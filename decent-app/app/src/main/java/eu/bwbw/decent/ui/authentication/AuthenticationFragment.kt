package eu.bwbw.decent.ui.authentication

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavDirections
import androidx.navigation.findNavController
import eu.bwbw.decent.R

class AuthenticationFragment : Fragment() {

    companion object {
        fun newInstance() = AuthenticationFragment()
    }

    private lateinit var viewModel: AuthenticationViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val root = inflater.inflate(R.layout.authentication_fragment, container, false)

        val buttonSendOrDeliver: Button = root.findViewById(R.id.button_save)
        buttonSendOrDeliver.setOnClickListener { view ->
            val directions: NavDirections = AuthenticationFragmentDirections.actionAuthenticationFragmentToSenderFragment()
            view.findNavController().navigate(directions)
        }

        return root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(AuthenticationViewModel::class.java)
    }

}
