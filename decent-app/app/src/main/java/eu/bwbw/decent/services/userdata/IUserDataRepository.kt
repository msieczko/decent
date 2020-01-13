package eu.bwbw.decent.services.userdata

import org.web3j.crypto.Credentials

interface IUserDataRepository {
    fun getCredentials(): Credentials
    fun isUserKeyPresent(): Boolean
    fun clearUserData()
}