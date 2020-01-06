package eu.bwbw.decent.ui.sender

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import eu.bwbw.decent.R
import eu.bwbw.decent.UserDataManager
import eu.bwbw.decent.ViewModelFactory
import eu.bwbw.decent.databinding.AddNewDeliveryFragmentBinding
import kotlinx.android.synthetic.main.add_new_delivery_fragment.*
import org.web3j.crypto.Credentials


class AddNewDeliveryFragment : Fragment() {

    companion object {
        fun newInstance() = AddNewDeliveryFragment()
    }

    private lateinit var viewModel: AddNewDeliveryViewModel
    private lateinit var binding: AddNewDeliveryFragmentBinding
    private lateinit var userDataManager: UserDataManager

    override fun onAttach(context: Context) {
        super.onAttach(context)
        userDataManager = UserDataManager(context)
    }

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

        viewModel = ViewModelProviders.of(this, ViewModelFactory.getInstance(this.activity!!.application)).get(AddNewDeliveryViewModel::class.java)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        // observers
        viewModel.deliverySaved.observe(this, Observer {
            this.findNavController().popBackStack()
        })

        viewModel.formValidationError.observe(this, Observer {
            if(it.isNotEmpty()) {
                errorInfo.text = it
                errorInfo.visibility = View.VISIBLE
            } else {
                errorInfo.visibility = View.GONE
                errorInfo.text = ""
            }
        })

        val addButton = binding.root.findViewById<Button>(R.id.button_add)
        addButton.setOnClickListener {
            val credentials = Credentials.create(userDataManager.userPrivateKey)
            binding.viewModel?.saveNewDelivery(credentials)
        }

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }

}
