package eu.bwbw.decent.ui.sender

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavDirections
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView
import eu.bwbw.decent.R
import eu.bwbw.decent.ViewModelFactory

class SenderFragment : Fragment() {

    private lateinit var senderViewModel: SenderViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_sender, container, false)
        senderViewModel =
            ViewModelProviders.of(this, ViewModelFactory.getInstance()).get(SenderViewModel::class.java)

        if (!senderViewModel.isUserKeyPresent()) {
            val directions: NavDirections = SenderFragmentDirections.actionSenderFragmentToAuthenticationFragment()
            findNavController().navigate(directions)
        }

        val fab: FloatingActionButton = root.findViewById(R.id.fab_add_delivery)
        fab.setOnClickListener { view ->
            val directions: NavDirections = SenderFragmentDirections.actionSenderFragmentToAddNewDeliveryFragment()
            view.findNavController().navigate(directions)
        }

        activity?.let {
            val navigationView =
                it.findViewById<View>(R.id.nav_view) as NavigationView
            val headerView = navigationView.getHeaderView(0)
            headerView.findViewById<TextView>(R.id.walletAddress).text =
                getString(R.string.my_eth_address, senderViewModel.getAddress())
        }

        return root
    }
}