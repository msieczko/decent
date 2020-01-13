package eu.bwbw.decent.ui.courier.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import eu.bwbw.decent.R
import eu.bwbw.decent.ViewModelFactory
import eu.bwbw.decent.databinding.FragmentDeliveryDetailsBinding
import eu.bwbw.decent.domain.DeliveryState
import kotlinx.android.synthetic.main.fragment_delivery_details.*


class DeliveryDetailsCourierFragment : Fragment() {

    private lateinit var viewModel: DeliveryDetailsCourierViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentDeliveryDetailsBinding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_delivery_details,
            container,
            false
        )

        viewModel = ViewModelProviders.of(this, ViewModelFactory.getInstance()).get(
            DeliveryDetailsCourierViewModel::class.java
        )

        viewModel.actionFinished.observe(this,
            Observer {
                this.findNavController().popBackStack()
            })

        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        arguments?.let {
            val safeArgs =
                DeliveryDetailsCourierFragmentArgs.fromBundle(it)
            viewModel.openDelivery(safeArgs.deliveryId)
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        if (viewModel.delivery.value?.state != DeliveryState.OFFER) {
            hiddenWhenNoAction.visibility = View.GONE
        } else {
            hiddenWhenNoAction.visibility = View.VISIBLE
        }

        actionButton.apply {
            text = "Pickup package"
            setOnClickListener {
                viewModel.pickupPackage()
            }
            visibility = View.VISIBLE
        }

    }
}
