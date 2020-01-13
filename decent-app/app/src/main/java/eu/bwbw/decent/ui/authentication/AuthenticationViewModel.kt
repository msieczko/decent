package eu.bwbw.decent.ui.authentication

import androidx.lifecycle.ViewModel
import org.web3j.crypto.ECKeyPair
import java.math.BigInteger

class AuthenticationViewModel : ViewModel() {
    fun isUsersKeyValid(key: String): Boolean {
        if (key.isEmpty()) {
            return false
        }
        try {
            ECKeyPair.create(BigInteger(key, 16))
        } catch (e: NumberFormatException) {
            return false
        }
        return true
    }

}
