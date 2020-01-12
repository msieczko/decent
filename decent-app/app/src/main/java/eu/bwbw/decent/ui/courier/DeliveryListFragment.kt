package eu.bwbw.decent.ui.courier

import android.view.View
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavDirections
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import eu.bwbw.decent.ViewModelFactory
import eu.bwbw.decent.ui.common.BaseDeliveriesViewModel
import eu.bwbw.decent.ui.common.BaseDeliveryListFragment


class DeliveryListFragment : BaseDeliveryListFragment<DeliveryRecyclerViewAdapter.ViewHolder>() {

    private lateinit var viewModel: CourierViewModel

    override suspend fun getRecyclerViewAdapter(view: View): RecyclerView.Adapter<DeliveryRecyclerViewAdapter.ViewHolder> {
        return DeliveryRecyclerViewAdapter(
            onDeliveryClick = {
                it?.let {
                    val directions: NavDirections =
                        CourierFragmentDirections.actionCourierFragmentToDeliveryDetailsCourierFragment(it.id)
                    view.findNavController().navigate(directions)
                }
            },
            values = viewModel.getDeliveries()
        )
    }

    override fun setupViewModel() {
        viewModel =
            ViewModelProviders.of(this, ViewModelFactory.getInstance())
                .get(CourierViewModel::class.java)
    }

    override fun getViewModel(): BaseDeliveriesViewModel {
        return viewModel
    }
}
