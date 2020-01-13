package eu.bwbw.decent.services.userdata

import android.content.Context
import org.web3j.crypto.Credentials

class UserDataRepository(context: Context): IUserDataRepository {
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

    override fun isUserKeyPresent(): Boolean {
        return userPrivateKey != ""
    }

    fun isGeneratedKeyPresent(): Boolean {
        return generatedPrivateKey != ""
    }

    override fun getCredentials(): Credentials {
        return Credentials.create(if (userPrivateKey.isNotEmpty()) userPrivateKey else generatedPrivateKey)
    }

    override fun clearUserData() {
        dataStore.edit().apply {
            remove(PRIVATE_KEY)
            remove(GENERATED_PRIVATE_KEY)
        }.apply()
    }

    companion object {
        private const val USER_DATA_KEY = "eu.bwbw.decent.USER_DATA_KEY"
        private val PRIVATE_KEY = "DATA_PRIVATE_KEY"
        private val GENERATED_PRIVATE_KEY = "DATA_GENERATED_PRIVATE_KEY"
    }
}
