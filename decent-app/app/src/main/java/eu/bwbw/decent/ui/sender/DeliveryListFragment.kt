package eu.bwbw.decent.ui.sender

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import eu.bwbw.decent.R
import eu.bwbw.decent.domain.Delivery


class DeliveryListFragment : Fragment() {

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

        // Set the adapter
        if (view is RecyclerView) {
            with(view) {
                layoutManager = LinearLayoutManager(context)
                adapter = DeliveryRecyclerViewAdapter(Delivery.LIST_MOCK)
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
