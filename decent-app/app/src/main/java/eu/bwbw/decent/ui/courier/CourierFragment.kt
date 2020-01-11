package eu.bwbw.decent.ui.courier

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
import eu.bwbw.decent.ViewModelFactory
import eu.bwbw.decent.services.UserDataManager

class CourierFragment : Fragment() {

    private lateinit var courierViewModel: CourierViewModel
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
        courierViewModel =
            ViewModelProviders.of(this, ViewModelFactory.getInstance())
                .get(CourierViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_courier, container, false)

        if (!userDataManager.isUserKeyPresent()) {
            val directions: NavDirections = CourierFragmentDirections.actionCourierFragmentToAuthenticationFragment()
            findNavController().navigate(directions)
        }

        val fab: FloatingActionButton = root.findViewById(R.id.fab_request_approval)
        fab.setOnClickListener { view ->
            val directions: NavDirections = CourierFragmentDirections.actionCourierFragmentToDeliveryApprovalRequest()
            view.findNavController().navigate(directions)
        }

        val textView: TextView = root.findViewById(R.id.text_courier)
        courierViewModel.text.observe(this, Observer {
            textView.text = it
        })
        return root
    }
}