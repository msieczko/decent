package eu.bwbw.decent.services

import org.web3j.crypto.Credentials

class UserDataMockRepository(
    private val credentials: Credentials
) : IUserDataRepository {

    override fun getCredentials(): Credentials {
        return credentials
    }

    override fun isUserKeyPresent() = true
}