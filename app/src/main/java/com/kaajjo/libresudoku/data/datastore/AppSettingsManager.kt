package com.kaajjo.libresudoku.data.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.kaajjo.libresudoku.core.PreferencesConstants
import com.kaajjo.libresudoku.ui.more.settings.autoupdate.UpdateChannel
import kotlinx.coroutines.flow.map
import javax.inject.Singleton

@Singleton
class AppSettingsManager(context: Context) {
    private val Context.createDataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")
    private val dataStore = context.createDataStore

    // first app launch
    private val firstLaunchKey = booleanPreferencesKey("first_launch")

    // highlight mistakes
    private val highlightMistakesKey = intPreferencesKey("mistakes_highlight")

    // highlight current position with horizontal and vertical lines
    private val positionLinesKey = booleanPreferencesKey("position_lines")


    // font size (0 automatic (default), 1 - small, 2 - medium, 3 - big)
    private val fontSizeKey = intPreferencesKey("board_font_size")



    private val autoUpdateChannelKey = intPreferencesKey("auto_update")
    private val updateDismissedNameKey = stringPreferencesKey("update_dismissed_name") // name of the update that was dismissed


    val firstLaunch = dataStore.data.map { preferences ->
        preferences[firstLaunchKey] != false
    }



    val highlightMistakes = dataStore.data.map { preferences ->
        preferences[highlightMistakesKey] ?: PreferencesConstants.DEFAULT_HIGHLIGHT_MISTAKES
    }



    suspend fun setPositionLines(enabled: Boolean) {
        dataStore.edit { settings ->
            settings[positionLinesKey] = enabled
        }
    }

    val positionLines = dataStore.data.map { preferences ->
        preferences[positionLinesKey] ?: PreferencesConstants.DEFAULT_POSITION_LINES
    }



    suspend fun setFontSize(value: Int) {
        dataStore.edit { settings ->
            settings[fontSizeKey] = value
        }
    }

    val fontSize = dataStore.data.map { preferences ->
        preferences[fontSizeKey] ?: PreferencesConstants.DEFAULT_FONT_SIZE_FACTOR
    }



    val autoUpdateChannel = dataStore.data.map { settings ->
        val channel = settings[autoUpdateChannelKey] ?: PreferencesConstants.DEFAULT_AUTOUPDATE_CHANNEL
        when (channel) {
            0 -> UpdateChannel.Disabled
            1 -> UpdateChannel.Stable
            2 -> UpdateChannel.Beta
            else -> UpdateChannel.Disabled
        }
    }

    suspend fun setAutoUpdateChannel(channel: UpdateChannel) {
        dataStore.edit { settings ->
            settings[autoUpdateChannelKey] = when (channel) {
                UpdateChannel.Disabled -> 0
                UpdateChannel.Stable -> 1
                UpdateChannel.Beta -> 2
            }
        }
    }

    val updateDismissedName = dataStore.data.map { settings ->
        settings[updateDismissedNameKey] ?: ""
    }

    suspend fun setUpdateDismissedName(name: String) {
        dataStore.edit { settings ->
            settings[updateDismissedNameKey] = name
        }
    }

}