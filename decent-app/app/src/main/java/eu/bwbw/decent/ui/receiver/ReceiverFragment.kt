package eu.bwbw.decent.ui.receiver

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import eu.bwbw.decent.R
import eu.bwbw.decent.services.UserDataManager
import eu.bwbw.decent.ViewModelFactory

class ReceiverFragment : Fragment() {

    private lateinit var receiverViewModel: ReceiverViewModel
    private lateinit var userDataManager: UserDataManager

    override fun onAttach(context: Context) {
        super.onAttach(context)
        userDataManager = UserDataManager(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        receiverViewModel =
            ViewModelProviders.of(this, ViewModelFactory.getInstance(this.activity!!.application)).get(ReceiverViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_receiver, container, false)

        if(userDataManager.isUserKeyPresent()) {
            receiverViewModel.privateKey = userDataManager.userPrivateKey
        } else {
            receiverViewModel.privateKey = userDataManager.generatedPrivateKey
        }

        val textView: TextView = root.findViewById(R.id.text_receiver)
        receiverViewModel.text.observe(this, Observer {
            textView.text = getString(R.string.my_eth_address, it)
        })
        return root
    }
}