package eu.bwbw.decent.ui.sender

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavDirections
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import eu.bwbw.decent.R
import eu.bwbw.decent.ViewModelFactory


class DeliveryListFragment : Fragment() {

    private lateinit var senderViewModel: SenderViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_delivery_list, container, false)

        senderViewModel =
            ViewModelProviders.of(this, ViewModelFactory.getInstance(this.activity!!.application))
                .get(SenderViewModel::class.java)

        // Set the adapter
        if (view is RecyclerView) {
            with(view) {
                layoutManager = LinearLayoutManager(context)
                adapter = DeliveryRecyclerViewAdapter(
                    onDeliveryClick = {
                        it?.let {
                            val directions: NavDirections =
                                SenderFragmentDirections.actionSenderFragmentToDeliveryDetailsFragment(it.id)
                            view.findNavController().navigate(directions)
                        }
                    },
                    values = senderViewModel.getDeliveries()
                )
            }
        }
        return view
    }


    override fun onDetach() {
        super.onDetach()
    }

    companion object {
        fun newInstance() = DeliveryListFragment

    }
}
