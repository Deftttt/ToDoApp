package com.example.todocomposeapp.data.repositories

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.todocomposeapp.data.models.Priority
import com.example.todocomposeapp.util.Constants
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.util.prefs.Preferences
import javax.inject.Inject

private val Context.dataStore by preferencesDataStore(name = Constants.PREFERENCE_NAME)

@ViewModelScoped
class DataStoreRepository @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private object PreferenceKeys {
        val sortState = stringPreferencesKey(Constants.PREFERENCE_KEY)
    }

    private val dataStore = context.dataStore


    suspend fun saveSortState(priority: Priority) {
        dataStore.edit { preferences ->
            preferences[PreferenceKeys.sortState] = priority.name
        }
    }

    val readSortState: Flow<String> = dataStore.data
        .catch { exception ->
            if (exception is Exception) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { preferences ->
            val sortState = preferences[PreferenceKeys.sortState] ?: Priority.NONE.name
            sortState
        }


}