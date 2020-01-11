package eu.bwbw.decent.ui.sender.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import eu.bwbw.decent.R
import eu.bwbw.decent.ViewModelFactory
import eu.bwbw.decent.databinding.FragmentDeliveryDetailsBinding
import eu.bwbw.decent.ui.sender.DeliveryDetailsSenderFragmentArgs


class DeliveryDetailsSenderFragment : Fragment() {

    private lateinit var viewModel: DeliveryDetailsSenderViewModel

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
        viewModel = ViewModelProviders.of(this, ViewModelFactory.getInstance(this.activity!!.application)).get(
            DeliveryDetailsSenderViewModel::class.java
        )

        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        arguments?.let {
            val safeArgs =
                DeliveryDetailsSenderFragmentArgs.fromBundle(it)
            viewModel.openDelivery(safeArgs.deliveryId)
        }

        return binding.root
    }

}