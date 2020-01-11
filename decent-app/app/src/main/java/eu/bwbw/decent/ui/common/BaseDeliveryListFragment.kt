package eu.bwbw.decent.ui.common

import android.content.Context
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
import eu.bwbw.decent.services.UserDataManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch

abstract class BaseDeliveryListFragment<T : RecyclerView.ViewHolder?> : Fragment() {

    private lateinit var deliveryRecyclerViewAdapter: RecyclerView.Adapter<T>
    protected lateinit var userDataManager: UserDataManager

    override fun onAttach(context: Context) {
        super.onAttach(context)
        userDataManager = UserDataManager(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_delivery_list, container, false)
        setHasOptionsMenu(true)

        setupViewModel()
        CoroutineScope(Main).launch {
            deliveryRecyclerViewAdapter = getRecyclerViewAdapter(view)
            // Set the adapter
            if (view is RecyclerView) {
                with(view) {
                    layoutManager = LinearLayoutManager(context)
                    adapter = deliveryRecyclerViewAdapter
                }
            }
        }

        getViewModel().deliveriesUpdated.observe(
            this,
            Observer {
                println(deliveryRecyclerViewAdapter.itemCount)
                deliveryRecyclerViewAdapter.notifyDataSetChanged()
            }
        )

        return view
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_refresh -> {
                getViewModel().updateDeliveries(userDataManager.getCredentials())
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    abstract suspend fun getRecyclerViewAdapter(view: View): RecyclerView.Adapter<T>
    abstract fun setupViewModel()
    abstract fun getViewModel(): BaseDeliveriesViewModel
}
