package eu.bwbw.decent.ui.sender

import android.view.View
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavDirections
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import eu.bwbw.decent.ViewModelFactory
import eu.bwbw.decent.ui.common.BaseDeliveriesViewModel
import eu.bwbw.decent.ui.common.BaseDeliveryListFragment


class DeliveryListFragment : BaseDeliveryListFragment<DeliveryRecyclerViewAdapter.ViewHolder>() {

    private lateinit var viewModel: SenderViewModel

    override suspend fun getRecyclerViewAdapter(view: View): RecyclerView.Adapter<DeliveryRecyclerViewAdapter.ViewHolder> {
        return DeliveryRecyclerViewAdapter(
            onDeliveryClick = {
                it?.let {
                    val directions: NavDirections =
                        SenderFragmentDirections.actionSenderFragmentToDeliveryDetailsSenderFragment(it.id)
                    view.findNavController().navigate(directions)
                }
            },
            onRemoveDeliveryClick = {
                it?.let {
                    viewModel.onRemoveDeliveryClick(it)
                }

            },
            values = viewModel.getDeliveries(userDataManager.getCredentials())
        )
    }

    override fun setupViewModel() {
        viewModel =
            ViewModelProviders.of(this, ViewModelFactory.getInstance(this.activity!!.application))
                .get(SenderViewModel::class.java)
    }

    override fun getViewModel(): BaseDeliveriesViewModel {
        return viewModel
    }
}
