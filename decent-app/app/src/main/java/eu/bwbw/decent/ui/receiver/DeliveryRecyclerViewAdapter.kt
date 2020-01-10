package eu.bwbw.decent.ui.receiver


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import eu.bwbw.decent.R
import eu.bwbw.decent.domain.Delivery
import kotlinx.android.synthetic.main.fragment_delivery_receiver.view.*

class DeliveryRecyclerViewAdapter(
    private val onDeliveryClick: (item: Delivery?) -> Unit,
    private val onConfirmDeliveryClick: (item: Delivery?) -> Unit,
    private val values: List<Delivery>
) : RecyclerView.Adapter<DeliveryRecyclerViewAdapter.ViewHolder>() {

    private val onClickListenerDelivery: View.OnClickListener
    private val onConfirmDeliveryListener: View.OnClickListener

    init {
        onClickListenerDelivery = View.OnClickListener { v ->
            val item = v.tag as Delivery
            onDeliveryClick(item)
        }

        onConfirmDeliveryListener = View.OnClickListener { v ->
            val item = v.tag as Delivery
            onConfirmDeliveryClick(item)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_delivery_receiver, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = values[position]
        holder.titleView.text = item.title
        holder.addressView.text = item.receiverPostalAddress
        holder.depositView.text = item.courierDeposit
        holder.maxDeliveryTimeView.text = "${item.maxDeliveryTime} h"

        with(holder.view) {
            tag = item
            setOnClickListener(onClickListenerDelivery)
        }

        with(holder.receivedButton) {
            tag = item
            setOnClickListener(onConfirmDeliveryListener)
        }
    }

    override fun getItemCount(): Int = values.size

    inner class ViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val titleView: TextView = view.title
        val addressView: TextView = view.receiver_postal_address
        val depositView: TextView = view.courier_deposit
        val maxDeliveryTimeView: TextView = view.max_delivery_time

        val receivedButton: Button = view.receivedButton

        override fun toString(): String {
            return super.toString() + " '" + titleView.text + "'"
        }
    }
}
