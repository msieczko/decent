package eu.bwbw.decent.ui.sender


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import eu.bwbw.decent.R
import eu.bwbw.decent.domain.Delivery
import kotlinx.android.synthetic.main.fragment_delivery.view.*

class DeliveryRecyclerViewAdapter(
    private val values: List<Delivery>
) : RecyclerView.Adapter<DeliveryRecyclerViewAdapter.ViewHolder>() {

    private val mOnClickListener: View.OnClickListener

    init {
        mOnClickListener = View.OnClickListener { v ->
            val item = v.tag as Delivery
            // Notify the active callbacks interface (the activity, if the fragment is attached to
            // one) that an item has been selected.
            //mListener?.onListFragmentInteraction(item)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_delivery, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = values[position]
        holder.titleView.text = item.title
        holder.addressView.text = item.receiverPostalAddress
        holder.depositView.text = item.courierDeposit.toString() + "zł"
        holder.awardView.text = item.courierAward.toString() + "zł"
        holder.maxDeliveryTimeView.text = item.maxDeliveryTime.toString() + "h"

        with(holder.view) {
            tag = item
            setOnClickListener(mOnClickListener)
        }
    }

    override fun getItemCount(): Int = values.size

    inner class ViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val titleView: TextView = view.title
        val addressView: TextView = view.receiver_postal_address
        val depositView: TextView = view.courier_deposit
        val awardView: TextView = view.courier_award
        val maxDeliveryTimeView: TextView = view.max_delivery_time

        override fun toString(): String {
            return super.toString() + " '" + titleView.text + "'"
        }
    }
}
