package eu.bwbw.decent.services.userdata

import android.content.Context
import org.web3j.crypto.Credentials

class UserDataRepository(context: Context) : IUserDataRepository {
    private val dataStore = context.getSharedPreferences(USER_DATA_KEY, Context.MODE_PRIVATE)

    var userPrivateKey: String? = null
        get() {
            if (field == null) {
                field = dataStore.getString(PRIVATE_KEY, null)
            }
            return field
        }
        set(key) {
            dataStore.edit().putString(PRIVATE_KEY, key).apply()
            field = key
        }

    var generatedPrivateKey: String? = null
        get() {
            if (field == null) {
                field = dataStore.getString(GENERATED_PRIVATE_KEY, null)
            }
            return field
        }
        set(key) {
            dataStore.edit().putString(GENERATED_PRIVATE_KEY, key).apply()
            field = key
        }

    override fun isUserKeyPresent(): Boolean {
        return userPrivateKey != null
    }

    override fun isGeneratedKeyPresent(): Boolean {
        return generatedPrivateKey != null
    }

    override fun getCredentials(): Credentials {
        val key = when {
            userPrivateKey != null -> userPrivateKey
            generatedPrivateKey != null -> generatedPrivateKey
            else -> throw Error("No key error")
        }

        return Credentials.create(key)
    }

    override fun clearUserData() {
        dataStore.edit().apply {
            remove(PRIVATE_KEY)
            remove(GENERATED_PRIVATE_KEY)
        }.apply()
        userPrivateKey = null
        generatedPrivateKey = null
    }

    companion object {
        private const val USER_DATA_KEY = "eu.bwbw.decent.USER_DATA_KEY"
        private val PRIVATE_KEY = "DATA_PRIVATE_KEY"
        private val GENERATED_PRIVATE_KEY = "DATA_GENERATED_PRIVATE_KEY"
    }
}
