package me.lokmvne.app1.dataStore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first

val Context.MyDataStore: DataStore<Preferences> by preferencesDataStore(name = "Settings")

class DataStoreRepo(context: Context) {
    private val dataStore = context.MyDataStore

    private object PreferencesKeys {
        val DARK_MODE = booleanPreferencesKey("DarkMode")
    }

    suspend fun changeMode(isdarkmode: Boolean): Boolean {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.DARK_MODE] = isdarkmode
        }
        return dataStore.data.first()[PreferencesKeys.DARK_MODE] ?: false
    }

    suspend fun getDarkMode(): Boolean {
        return dataStore.data.first()[PreferencesKeys.DARK_MODE] ?: false
    }
}