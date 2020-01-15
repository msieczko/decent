package eu.bwbw.decent.ui.authentication

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavDirections
import androidx.navigation.findNavController
import eu.bwbw.decent.R
import eu.bwbw.decent.services.userdata.UserDataRepository
import org.web3j.utils.Numeric

class AuthenticationFragment : Fragment() {

    private lateinit var userDataRepository: UserDataRepository

    private lateinit var viewModel: AuthenticationViewModel

    override fun onAttach(context: Context) {
        super.onAttach(context)
        userDataRepository = UserDataRepository(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val root = inflater.inflate(R.layout.authentication_fragment, container, false)

        val buttonSendOrDeliver: Button = root.findViewById(R.id.button_save)
        buttonSendOrDeliver.setOnClickListener { view ->
            val key = root.findViewById<EditText>(R.id.usersKey).text.toString()
            val unprefixedKey = Numeric.cleanHexPrefix(key)
            if(viewModel.isUsersKeyValid(unprefixedKey)) {
                userDataRepository.userPrivateKey = unprefixedKey
                val directions: NavDirections = AuthenticationFragmentDirections.actionAuthenticationFragmentToSenderFragment()
                view.findNavController().navigate(directions)
            } else {
                root.findViewById<TextView>(R.id.errorInfo).visibility = View.VISIBLE
            }
        }

        return root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(AuthenticationViewModel::class.java)
    }

}
