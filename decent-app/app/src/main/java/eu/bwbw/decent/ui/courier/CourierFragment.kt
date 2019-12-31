package eu.bwbw.decent.ui.courier

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import eu.bwbw.decent.R

class CourierFragment : Fragment() {

    private lateinit var courierViewModel: CourierViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        courierViewModel =
            ViewModelProviders.of(this).get(CourierViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_courier, container, false)
        val textView: TextView = root.findViewById(R.id.text_courier)
        courierViewModel.text.observe(this, Observer {
            textView.text = it
        })
        return root
    }
}