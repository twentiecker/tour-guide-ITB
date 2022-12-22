package com.twentiecker.storyapp.model

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class UserPreference private constructor(private val dataStore: DataStore<Preferences>) {

    fun getUser(): Flow<UserModel> {
        return dataStore.data.map { preferences ->
            UserModel(
                preferences[USERID_KEY] ?: "",
                preferences[NAME_KEY] ?: "",
                preferences[TOKEN_KEY] ?: "",
                preferences[STATE_KEY] ?: false
            )
        }
    }

    fun getBike(): Flow<BikeModel> {
        return dataStore.data.map { preferences ->
            BikeModel(
                preferences[NAME] ?: "",
                preferences[DESCRIPTION] ?: "",
                preferences[PHOTO] ?: "",
                preferences[ENERGY] ?: "",
                preferences[RATING] ?: "",
                preferences[SPEED] ?: "",
                preferences[STATE_SCAN] ?: false,
            )
        }
    }

    suspend fun saveUser(user: UserModel) {
        dataStore.edit { preferences ->
            preferences[USERID_KEY] = user.userId
            preferences[NAME_KEY] = user.name
            preferences[TOKEN_KEY] = user.token
            preferences[STATE_KEY] = user.isLogin
        }
    }

    suspend fun saveBike(bikeModel: BikeModel) {
        dataStore.edit { preferences ->
            preferences[NAME] = bikeModel.name
            preferences[DESCRIPTION] = bikeModel.description
            preferences[ENERGY] = bikeModel.energy
            preferences[PHOTO] = bikeModel.photo
            preferences[RATING] = bikeModel.rating
            preferences[SPEED] = bikeModel.speed
            preferences[STATE_SCAN] = bikeModel.isScanned
        }
    }

    suspend fun login() {
        dataStore.edit { preferences ->
            preferences[STATE_KEY] = true
        }
    }

    suspend fun scan() {
        dataStore.edit { preferences ->
            preferences[STATE_SCAN] = true
        }
    }

    suspend fun logout() {
        dataStore.edit { preferences ->
            preferences[STATE_KEY] = false
            preferences[NAME] = ""
            preferences[DESCRIPTION] = ""
            preferences[ENERGY] = ""
            preferences[PHOTO] = ""
            preferences[RATING] = ""
            preferences[SPEED] = ""
            preferences[STATE_SCAN] = false
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: UserPreference? = null

        private val USERID_KEY = stringPreferencesKey("userid")
        private val NAME_KEY = stringPreferencesKey("name")
        private val TOKEN_KEY = stringPreferencesKey("token")
        private val STATE_KEY = booleanPreferencesKey("state")

        private val NAME = stringPreferencesKey("bike_name")
        private val DESCRIPTION = stringPreferencesKey("description")
        private val ENERGY = stringPreferencesKey("energy")
        private val PHOTO = stringPreferencesKey("photo")
        private val RATING = stringPreferencesKey("rating")
        private val SPEED = stringPreferencesKey("speed")
        private val STATE_SCAN = booleanPreferencesKey("scan")

        fun getInstance(dataStore: DataStore<Preferences>): UserPreference {
            return INSTANCE ?: synchronized(this) {
                val instance = UserPreference(dataStore)
                INSTANCE = instance
                instance
            }
        }
    }
}