package eu.bwbw.decent

import android.annotation.SuppressLint
import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import eu.bwbw.decent.domain.EthAddress
import eu.bwbw.decent.services.*
import eu.bwbw.decent.ui.common.DeliveryDetailsViewModel
import eu.bwbw.decent.ui.courier.CourierViewModel
import eu.bwbw.decent.ui.receiver.ReceiverViewModel
import eu.bwbw.decent.ui.sender.AddNewDeliveryViewModel
import eu.bwbw.decent.ui.sender.SenderViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.web3j.crypto.Credentials
import org.web3j.protocol.Web3j
import org.web3j.protocol.http.HttpService

@Suppress("UNCHECKED_CAST")
class ViewModelFactory private constructor() : ViewModelProvider.NewInstanceFactory() {

    private val courierServiceContractAddress = "A193E42526F1FEA8C99AF609dcEabf30C1c29fAA"
    private val web3j = Web3j.build(
        HttpService("http://10.0.2.2:8545") // TODO move to properties
    )
    private val deliveryDetailsRepository = DeliveryDetailsMemoryRepository()
    private val deliveriesService = DeliveriesService(
        deliveryDetailsRepository,
        courierServiceContractAddress,
        web3j
    )

    override fun <T : ViewModel> create(modelClass: Class<T>) =
        with(modelClass) {
            when {
                isAssignableFrom(AddNewDeliveryViewModel::class.java) ->
                    AddNewDeliveryViewModel(courierServiceContractAddress, web3j, deliveryDetailsRepository)
                isAssignableFrom(SenderViewModel::class.java) ->
                    SenderViewModel(deliveriesService)
                isAssignableFrom(CourierViewModel::class.java) ->
                    CourierViewModel(deliveriesService)
                isAssignableFrom(ReceiverViewModel::class.java) ->
                    ReceiverViewModel(deliveriesService)
                isAssignableFrom(DeliveryDetailsViewModel::class.java) ->
                    DeliveryDetailsViewModel(deliveriesService)
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