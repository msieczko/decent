package eu.bwbw.decent.ui.courier


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import eu.bwbw.decent.R
import eu.bwbw.decent.domain.Delivery
import kotlinx.android.synthetic.main.fragment_delivery_courier.view.*

class DeliveryRecyclerViewAdapter(
    private val onDeliveryClick: (item: Delivery?) -> Unit,
    private val onPickupDeliveryClick: (item: Delivery?) -> Unit,
    private val values: List<Delivery>
) : RecyclerView.Adapter<DeliveryRecyclerViewAdapter.ViewHolder>() {

    private val onClickListenerDelivery: View.OnClickListener
    private val onPickupClickListener: View.OnClickListener

    init {
        onClickListenerDelivery = View.OnClickListener { v ->
            val item = v.tag as Delivery
            onDeliveryClick(item)
        }

        onPickupClickListener = View.OnClickListener { v ->
            val item = v.tag as Delivery
            onPickupDeliveryClick(item)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_delivery_courier, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = values[position]
        holder.titleView.text = item.title
        holder.addressView.text = item.receiverPostalAddress
        holder.depositView.text = item.courierDeposit
        holder.awardView.text = item.courierAward
        holder.maxDeliveryTimeView.text = "${item.maxDeliveryTime} h"

        with(holder.view) {
            tag = item
            setOnClickListener(onClickListenerDelivery)
        }

        with(holder.pickupButton) {
            tag = item
            setOnClickListener(onPickupClickListener)
        }
    }

    override fun getItemCount(): Int = values.size

    inner class ViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val titleView: TextView = view.title
        val addressView: TextView = view.receiver_postal_address
        val depositView: TextView = view.courier_deposit
        val awardView: TextView = view.courier_award
        val maxDeliveryTimeView: TextView = view.max_delivery_time

        val pickupButton: Button = view.receivedButton

        override fun toString(): String {
            return super.toString() + " '" + titleView.text + "'"
        }
    }
}
