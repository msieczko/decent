package eu.bwbw.decent.ui.receiver

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import eu.bwbw.decent.R
import eu.bwbw.decent.ViewModelFactory

class ReceiverFragment : Fragment() {

    private lateinit var receiverViewModel: ReceiverViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        receiverViewModel =
            ViewModelProviders.of(this, ViewModelFactory.getInstance()).get(ReceiverViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_receiver, container, false)

        val textView: TextView = root.findViewById(R.id.text_receiver)
        receiverViewModel.text.observe(this, Observer {
            textView.text = getString(R.string.my_eth_address, it)
        })
        return root
    }
}