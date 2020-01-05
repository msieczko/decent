package eu.bwbw.decent.ui.sender

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavDirections
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.google.android.material.floatingactionbutton.FloatingActionButton
import eu.bwbw.decent.R
import eu.bwbw.decent.UserDataManager
import eu.bwbw.decent.ViewModelFactory

class SenderFragment : Fragment() {

    private lateinit var senderViewModel: SenderViewModel
    private lateinit var userDataManager: UserDataManager

    override fun onAttach(context: Context) {
        super.onAttach(context)
        userDataManager = UserDataManager(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_sender, container, false)

        if (!userDataManager.isUserKeyPresent()) {
            val directions: NavDirections = SenderFragmentDirections.actionSenderFragmentToAuthenticationFragment()
            findNavController().navigate(directions)
        }

        val fab: FloatingActionButton = root.findViewById(R.id.fab_add_delivery)
        fab.setOnClickListener { view ->
            val directions: NavDirections = SenderFragmentDirections.actionSenderFragmentToAddNewDeliveryFragment()
            view.findNavController().navigate(directions)
        }

        senderViewModel =
            ViewModelProviders.of(this, ViewModelFactory.getInstance(this.activity!!.application)).get(SenderViewModel::class.java)

        val textView: TextView = root.findViewById(R.id.text_sender)
        senderViewModel.text.observe(this, Observer {
            textView.text = it
        })
        return root
    }
}