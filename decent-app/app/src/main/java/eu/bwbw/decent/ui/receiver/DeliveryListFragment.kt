package eu.bwbw.decent.ui.receiver

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import eu.bwbw.decent.R
import eu.bwbw.decent.ViewModelFactory


class DeliveryListFragment : Fragment() {

    private lateinit var receiverViewModel: ReceiverViewModel

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
        setHasOptionsMenu(true);

        receiverViewModel =
            ViewModelProviders.of(this, ViewModelFactory.getInstance(this.activity!!.application))
                .get(ReceiverViewModel::class.java)

        val deliveryRecyclerViewAdapter = DeliveryRecyclerViewAdapter(
            onDeliveryClick = { },
            values = receiverViewModel.getDeliveries()
        )

        // Set the adapter
        if (view is RecyclerView) {
            with(view) {
                layoutManager = LinearLayoutManager(context)

                adapter = deliveryRecyclerViewAdapter
            }
        }

        receiverViewModel.deliveriesUpdated.observe(
            this,
            Observer {
                deliveryRecyclerViewAdapter.notifyDataSetChanged()
            }
        )
        return view
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_refresh -> {
                receiverViewModel.updateDeliveries()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    companion object {
        fun newInstance() = DeliveryListFragment

    }
}
