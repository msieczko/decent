package eu.bwbw.decent.ui.welcome

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.navigation.NavDirections
import androidx.navigation.findNavController
import eu.bwbw.decent.R

class WelcomeFragment : Fragment() {

    companion object {
        fun newInstance() = WelcomeFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val root = inflater.inflate(R.layout.welcome_fragment, container, false)
        val actionBar = (activity as AppCompatActivity).supportActionBar
        actionBar?.hide()

        val buttonJustReceive: Button = root.findViewById(R.id.button_just_receive)
        buttonJustReceive.setOnClickListener { view ->
            val directions: NavDirections = WelcomeFragmentDirections.actionWelcomeFragmentToReceiverFragment()
            view.findNavController().navigate(directions)
            actionBar?.show()
        }

        val buttonSendOrDeliver: Button = root.findViewById(R.id.button_send_or_deliver)
        buttonSendOrDeliver.setOnClickListener { view ->
            val directions: NavDirections = WelcomeFragmentDirections.actionWelcomeFragmentToAuthenticationFragment()
            view.findNavController().navigate(directions)
            actionBar?.show()
        }

        return root
    }
}
