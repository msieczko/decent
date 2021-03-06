package eu.bwbw.decent.ui.sender.addnewdelivery

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Button
import android.widget.ScrollView
import android.widget.Spinner
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import eu.bwbw.decent.R
import eu.bwbw.decent.ViewModelFactory
import eu.bwbw.decent.databinding.AddNewDeliveryFragmentBinding
import kotlinx.android.synthetic.main.add_new_delivery_fragment.*


class AddNewDeliveryFragment : Fragment() {

    companion object {
        fun newInstance() = AddNewDeliveryFragment()
    }

    private lateinit var viewModel: AddNewDeliveryViewModel
    private lateinit var binding: AddNewDeliveryFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.add_new_delivery_fragment,
            container,
            false
        )

        viewModel = ViewModelProviders.of(this, ViewModelFactory.getInstance())
            .get(AddNewDeliveryViewModel::class.java)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        // observers
        viewModel.deliverySaved.observe(this, Observer {
            this.findNavController().popBackStack()
        })

        viewModel.formValidationError.observe(this, Observer {
            if (it.isNotEmpty()) {
                errorInfo.text = it
                errorInfo.visibility = View.VISIBLE
                val scrollView = binding.root.findViewById<ScrollView>(R.id.addNewDeliveryScrollView)
                scrollView.scrollTo(0, scrollView.top)
            } else {
                errorInfo.visibility = View.GONE
                errorInfo.text = ""
            }
        })

        val addButton = binding.root.findViewById<Button>(R.id.button_add)
        addButton.setOnClickListener {
            binding.viewModel?.saveNewDelivery()
        }

        bindSpinner(R.id.spinner_courier_deposit_units, R.array.eth_denominations) { viewModel.courierDepositUnit = it }
        bindSpinner(R.id.spinner_courier_award_units, R.array.eth_denominations) { viewModel.courierAwardUnit = it }
        bindSpinner(R.id.spinner_max_delivery_time_units, R.array.time_units) { viewModel.maxDeliveryTimeUnit = it }

        return binding.root
    }

    private fun bindSpinner(
        spinnerResourceId: Int,
        spinnerValuesResourceId: Int,
        bindFunction: (value: String) -> Unit
    ) {
        val spinner = binding.root.findViewById<Spinner>(spinnerResourceId)
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val spinnerValues = resources.getStringArray(spinnerValuesResourceId)
                bindFunction(spinnerValues[position])
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // should never happen
            }
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }

}
