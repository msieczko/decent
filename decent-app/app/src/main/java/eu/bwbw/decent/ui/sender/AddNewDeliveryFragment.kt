package eu.bwbw.decent.ui.sender

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import eu.bwbw.decent.R
import kotlinx.android.synthetic.main.app_bar_main.*


class AddNewDeliveryFragment : Fragment() {

    companion object {
        fun newInstance() = AddNewDeliveryFragment()
    }

    private lateinit var viewModel: AddNewDeliveryViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.add_new_delivery_fragment, container, false)

        val addButton = root.findViewById<Button>(R.id.button_add)
        addButton.setOnClickListener {
            it.findNavController().popBackStack()
        }

        return root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(AddNewDeliveryViewModel::class.java)
        // TODO: Use the ViewModel
    }

}
