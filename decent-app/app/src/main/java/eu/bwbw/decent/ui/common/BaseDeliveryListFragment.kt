package eu.bwbw.decent.ui.common

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import eu.bwbw.decent.R
import kotlinx.android.synthetic.main.fragment_delivery_list.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch

abstract class BaseDeliveryListFragment<T : RecyclerView.ViewHolder?> : Fragment() {

    private lateinit var deliveryRecyclerViewAdapter: RecyclerView.Adapter<T>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_delivery_list, container, false)
        setHasOptionsMenu(true)

        setupViewModel()

        val recyclerView = root.findViewById<RecyclerView>(R.id.list)
        CoroutineScope(Main).launch {
            deliveryRecyclerViewAdapter = getRecyclerViewAdapter(recyclerView)
            // Set the adapter
            with(recyclerView) {
                layoutManager = LinearLayoutManager(context)
                adapter = deliveryRecyclerViewAdapter
            }
        }

        getViewModel().deliveriesUpdated.observe(
            this,
            Observer {
                println(deliveryRecyclerViewAdapter.itemCount)
                deliveryRecyclerViewAdapter.notifyDataSetChanged()
            }
        )

        return root
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

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        getViewModel().isLoading.observe(this, Observer {
            progressBarDeliveryList.visibility = if (it) View.VISIBLE else View.GONE
        })
    }

    abstract suspend fun getRecyclerViewAdapter(view: View): RecyclerView.Adapter<T>
    abstract fun setupViewModel()
    abstract fun getViewModel(): BaseDeliveriesViewModel
}
