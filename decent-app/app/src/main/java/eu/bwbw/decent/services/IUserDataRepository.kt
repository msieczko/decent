package eu.bwbw.decent.services

import org.web3j.crypto.Credentials

interface IUserDataRepository {
    fun getCredentials(): Credentials
    fun isUserKeyPresent(): Boolean
}