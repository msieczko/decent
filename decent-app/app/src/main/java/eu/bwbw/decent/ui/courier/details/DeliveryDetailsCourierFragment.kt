package eu.bwbw.decent.ui.courier.details

import android.content.Context
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
import eu.bwbw.decent.services.UserDataManager
import kotlinx.android.synthetic.main.fragment_delivery_details.*


class DeliveryDetailsCourierFragment : Fragment() {

    private lateinit var viewModel: DeliveryDetailsCourierViewModel
    private lateinit var userDataManager: UserDataManager

    override fun onAttach(context: Context) {
        super.onAttach(context)
        userDataManager = UserDataManager(context)
    }

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
        actionButton.apply {
            text = "Pickup package"
            setOnClickListener {
                viewModel.pickupPackage(userDataManager.getCredentials())
            }
            visibility = View.VISIBLE
        }

    }
}
