package eu.bwbw.decent.ui.welcome

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.NavDirections
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import eu.bwbw.decent.R
import eu.bwbw.decent.services.userdata.UserDataRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.web3j.crypto.Credentials
import org.web3j.crypto.ECKeyPair
import org.web3j.crypto.Keys
import org.web3j.utils.Numeric

class WelcomeFragment : Fragment() {

    private lateinit var userDataRepository: UserDataRepository

    override fun onAttach(context: Context) {
        super.onAttach(context)
        userDataRepository = UserDataRepository(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val root = inflater.inflate(R.layout.welcome_fragment, container, false)
        val actionBar = (activity as AppCompatActivity).supportActionBar
        actionBar?.hide()

        if (userDataRepository.isUserKeyPresent()) {
            val directions: NavDirections = WelcomeFragmentDirections.actionWelcomeFragmentToSenderFragment()
            findNavController().navigate(directions)
            actionBar?.show()
        }

        if (userDataRepository.isGeneratedKeyPresent()) {
            val directions: NavDirections = WelcomeFragmentDirections.actionWelcomeFragmentToReceiverFragment()
            findNavController().navigate(directions)
            actionBar?.show()
        }

        val buttonJustReceive: Button = root.findViewById(R.id.button_just_receive)
        buttonJustReceive.setOnClickListener { view ->
            CoroutineScope(Dispatchers.Main).launch {
                generateKeys()
                val directions: NavDirections = WelcomeFragmentDirections.actionWelcomeFragmentToReceiverFragment()
                view.findNavController().navigate(directions)
                actionBar?.show()
            }
        }

        val buttonSendOrDeliver: Button = root.findViewById(R.id.button_send_or_deliver)
        buttonSendOrDeliver.setOnClickListener { view ->
            val directions: NavDirections =
                WelcomeFragmentDirections.actionWelcomeFragmentToAuthenticationFragment()
            view.findNavController().navigate(directions)
            actionBar?.show()
        }

        return root
    }

    private suspend fun generateKeys() {
        withContext(Dispatchers.Default) {
            val ecKeyPair: ECKeyPair = Keys.createEcKeyPair()
            val key = Numeric.toHexStringNoPrefix(ecKeyPair.privateKey)
            println("Receiver address = ${Credentials.create(key).address}")
            println("Receiver private key = $key")
            userDataRepository.generatedPrivateKey = key
        }
    }
}
