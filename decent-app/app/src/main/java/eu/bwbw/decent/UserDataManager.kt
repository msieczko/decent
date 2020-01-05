package eu.bwbw.decent

import android.content.Context

class UserDataManager(context: Context) {
    private val dataStore = context.getSharedPreferences(USER_DATA_KEY, Context.MODE_PRIVATE)

    var userPrivateKey: String = dataStore.getString(PRIVATE_KEY, "") ?: ""
        set(key) {
            dataStore.edit().putString(PRIVATE_KEY, key).apply()
            field = key
        }

    var generatedPrivateKey: String = dataStore.getString(GENERATED_PRIVATE_KEY, "") ?: ""
        set(key) {
            dataStore.edit().putString(GENERATED_PRIVATE_KEY, key).apply()
            field = key
        }

    fun isUserKeyPresent(): Boolean {
        return userPrivateKey != ""
    }

    fun isGeneratedKeyPresent(): Boolean {
        return generatedPrivateKey != ""
    }

    companion object {
        private const val USER_DATA_KEY = "eu.bwbw.decent.USER_DATA_KEY"
        private val PRIVATE_KEY = "DATA_PRIVATE_KEY"
        private val GENERATED_PRIVATE_KEY = "DATA_GENERATED_PRIVATE_KEY"
    }
}