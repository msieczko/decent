package eu.bwbw.decent

import android.annotation.SuppressLint
import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import eu.bwbw.decent.services.DeliveriesRepository
import eu.bwbw.decent.ui.sender.AddNewDeliveryViewModel
import eu.bwbw.decent.ui.common.DeliveryDetailsViewModel
import eu.bwbw.decent.ui.courier.CourierViewModel
import eu.bwbw.decent.ui.receiver.ReceiverViewModel
import eu.bwbw.decent.ui.sender.SenderViewModel
import org.web3j.protocol.Web3j
import org.web3j.protocol.http.HttpService

@Suppress("UNCHECKED_CAST")
class ViewModelFactory private constructor() : ViewModelProvider.NewInstanceFactory() {

    private val courierServiceContractAddress = "A193E42526F1FEA8C99AF609dcEabf30C1c29fAA"
    private val web3j = Web3j.build(
        HttpService("http://10.0.2.2:8545") // TODO move to properties
    )
    private val deliveriesRepository = DeliveriesRepository()

    override fun <T : ViewModel> create(modelClass: Class<T>) =
        with(modelClass) {
            when {
                isAssignableFrom(AddNewDeliveryViewModel::class.java) ->
                    AddNewDeliveryViewModel(courierServiceContractAddress, web3j)
                isAssignableFrom(SenderViewModel::class.java) ->
                    SenderViewModel(deliveriesRepository)
                isAssignableFrom(CourierViewModel::class.java) ->
                    CourierViewModel(deliveriesRepository)
                isAssignableFrom(ReceiverViewModel::class.java) ->
                    ReceiverViewModel(deliveriesRepository)
                isAssignableFrom(DeliveryDetailsViewModel::class.java) ->
                    DeliveryDetailsViewModel(deliveriesRepository)
                else ->
                    throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
            }
        } as T


    companion object {

        @SuppressLint("StaticFieldLeak")
        @Volatile
        private var INSTANCE: ViewModelFactory? = null

        fun getInstance(application: Application) =
            INSTANCE ?: synchronized(ViewModelFactory::class.java) {
                INSTANCE ?: ViewModelFactory()
                    .also { INSTANCE = it }
            }
    }
}