package com.example.inventory.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.handlers.ReplaceFileCorruptionHandler
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(
    name = "",
    corruptionHandler = ReplaceFileCorruptionHandler(
        produceNewData = { emptyPreferences() }
    )
)

class ChoiceRepository(private val context: Context) {
    private object PreferenceKeys {
        val SHOW_ENGLISH = booleanPreferencesKey("show_english")
        val SHOW_MONGOLIAN = booleanPreferencesKey("show_mongolian")
        val SHOW_BOTH = booleanPreferencesKey("show_both")
    }

    val showEnglish: Flow<Boolean> = context.dataStore.data
        .map { preferences ->
            preferences[PreferenceKeys.SHOW_ENGLISH] ?: false
        }

    val showMongolian: Flow<Boolean> = context.dataStore.data
        .map { preferences ->
            preferences[PreferenceKeys.SHOW_MONGOLIAN] ?: false
        }

    val showBoth: Flow<Boolean> = context.dataStore.data
        .map { preferences ->
            val showEnglish = preferences[PreferenceKeys.SHOW_ENGLISH] ?: false
            val showMongolian = preferences[PreferenceKeys.SHOW_MONGOLIAN] ?: false
            (preferences[PreferenceKeys.SHOW_BOTH] ?: true) && !showEnglish && !showMongolian
        }

    suspend fun updateAllChoice(
        showEnglish: Boolean,
        showMongolian: Boolean,
        showBoth: Boolean
    ) {
        context.dataStore.edit { preferences ->
            preferences[PreferenceKeys.SHOW_ENGLISH] = showEnglish
            preferences[PreferenceKeys.SHOW_MONGOLIAN] = showMongolian
            preferences[PreferenceKeys.SHOW_BOTH] = showBoth
        }
    }
    }