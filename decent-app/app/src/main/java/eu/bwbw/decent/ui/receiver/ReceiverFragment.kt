package eu.bwbw.decent.ui.receiver

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import eu.bwbw.decent.R
import eu.bwbw.decent.ViewModelFactory
import eu.bwbw.decent.databinding.FragmentReceiverBinding

class ReceiverFragment : Fragment() {

    private lateinit var receiverViewModel: ReceiverViewModel
    private lateinit var binding: FragmentReceiverBinding


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_receiver,
            container,
            false
        )
        receiverViewModel =
            ViewModelProviders.of(this, ViewModelFactory.getInstance()).get(ReceiverViewModel::class.java)
        binding.lifecycleOwner = this
        binding.viewModel = receiverViewModel

        return binding.root
    }
}