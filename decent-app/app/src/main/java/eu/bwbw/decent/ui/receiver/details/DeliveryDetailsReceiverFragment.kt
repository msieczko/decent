package eu.bwbw.decent.ui.receiver.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavDirections
import androidx.navigation.findNavController
import eu.bwbw.decent.R
import eu.bwbw.decent.ViewModelFactory
import eu.bwbw.decent.databinding.FragmentDeliveryDetailsBinding
import kotlinx.android.synthetic.main.fragment_delivery_details.*


class DeliveryDetailsReceiverFragment : Fragment() {

    private lateinit var viewModel: DeliveryDetailsReceiverViewModel

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
            DeliveryDetailsReceiverViewModel::class.java
        )

        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        arguments?.let {
            val safeArgs =
                DeliveryDetailsReceiverFragmentArgs.fromBundle(
                    it
                )
            viewModel.openDelivery(safeArgs.deliveryId)
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        actionButton.apply {
            text = "Approve package delivery"
            setOnClickListener {
                val directions: NavDirections =
                    DeliveryDetailsReceiverFragmentDirections.actionDeliveryDetailsReceiverFragmentToApprovePackage(
                        viewModel.delivery.value?.title ?: "no title"
                    )
                view.findNavController().navigate(directions)
            }
            visibility = View.VISIBLE
        }

    }

}
