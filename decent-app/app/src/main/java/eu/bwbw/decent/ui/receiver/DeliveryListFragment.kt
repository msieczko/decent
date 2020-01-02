package eu.bwbw.decent.ui.receiver

import android.view.View
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.RecyclerView
import eu.bwbw.decent.ViewModelFactory
import eu.bwbw.decent.ui.common.BaseDeliveriesViewModel
import eu.bwbw.decent.ui.common.BaseDeliveryListFragment


class DeliveryListFragment : BaseDeliveryListFragment<DeliveryRecyclerViewAdapter.ViewHolder>() {

    lateinit var viewModel: ReceiverViewModel

    override fun setupViewModel() {
        viewModel =
            ViewModelProviders.of(this, ViewModelFactory.getInstance(this.activity!!.application))
                .get(ReceiverViewModel::class.java)
    }

    override fun getRecyclerViewAdapter(view: View): RecyclerView.Adapter<DeliveryRecyclerViewAdapter.ViewHolder> {
        return DeliveryRecyclerViewAdapter(
            onDeliveryClick = { },
            values = viewModel.getDeliveries()
        )
    }

    override fun getViewModel(): BaseDeliveriesViewModel {
        return viewModel
    }
}
