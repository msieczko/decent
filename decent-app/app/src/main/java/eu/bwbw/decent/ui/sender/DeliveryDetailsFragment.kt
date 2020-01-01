package eu.bwbw.decent.ui.sender

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import eu.bwbw.decent.R
import eu.bwbw.decent.ViewModelFactory
import eu.bwbw.decent.databinding.DeliveryDetailsFragmentBinding

class DeliveryDetailsFragment : Fragment() {

    companion object {
        fun newInstance() = DeliveryDetailsFragment()
    }

    private lateinit var viewModel: DeliveryDetailsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding : DeliveryDetailsFragmentBinding = DataBindingUtil.inflate(
            inflater,
            R.layout.delivery_details_fragment,
            container,
            false
        )
        viewModel = ViewModelProviders.of(this, ViewModelFactory.getInstance(this.activity!!.application)).get(DeliveryDetailsViewModel::class.java)

        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        arguments?.let {
            val safeArgs = DeliveryDetailsFragmentArgs.fromBundle(it)
            viewModel.openDelivery(safeArgs.deliveryId)
        }

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }

}
