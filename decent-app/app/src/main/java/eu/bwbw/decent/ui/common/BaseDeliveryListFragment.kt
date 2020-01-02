package eu.bwbw.decent.ui.common

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import eu.bwbw.decent.R

abstract class BaseDeliveryListFragment<T : RecyclerView.ViewHolder?> : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_delivery_list, container, false)
        setHasOptionsMenu(true)

        setupViewModel()
        val deliveryRecyclerViewAdapter = getRecyclerViewAdapter(view)

        // Set the adapter
        if (view is RecyclerView) {
            with(view) {
                layoutManager = LinearLayoutManager(context)
                adapter = deliveryRecyclerViewAdapter
            }
        }

        getViewModel().deliveriesUpdated.observe(
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
                getViewModel().updateDeliveries()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    abstract fun getRecyclerViewAdapter(view: View): RecyclerView.Adapter<T>
    abstract fun setupViewModel()
    abstract fun getViewModel(): BaseDeliveriesViewModel
}
