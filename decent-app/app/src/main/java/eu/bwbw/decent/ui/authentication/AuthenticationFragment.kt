package eu.bwbw.decent.ui.authentication

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavDirections
import androidx.navigation.findNavController
import eu.bwbw.decent.R
import eu.bwbw.decent.services.userdata.UserDataRepository
import kotlinx.android.synthetic.main.authentication_fragment.*
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
        return inflater.inflate(R.layout.authentication_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        button_save.setOnClickListener { it ->
            val unprefixedKey = Numeric.cleanHexPrefix(usersKey.text.toString())
            if(viewModel.isUsersKeyValid(unprefixedKey)) {
                userDataRepository.userPrivateKey = unprefixedKey
                button_save.onEditorAction(EditorInfo.IME_ACTION_DONE);
                val directions: NavDirections = AuthenticationFragmentDirections.actionAuthenticationFragmentToSenderFragment()
                it.findNavController().navigate(directions)
            } else {
                errorInfo.visibility = View.VISIBLE
            }
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(AuthenticationViewModel::class.java)
    }

}
