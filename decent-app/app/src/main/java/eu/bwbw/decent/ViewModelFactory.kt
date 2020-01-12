package eu.bwbw.decent

import android.annotation.SuppressLint
import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import eu.bwbw.decent.domain.EthAddress
import eu.bwbw.decent.services.*
import eu.bwbw.decent.services.userdata.UserDataMockRepository
import eu.bwbw.decent.services.userdata.UserDataRepository
import eu.bwbw.decent.ui.courier.CourierViewModel
import eu.bwbw.decent.ui.courier.details.DeliveryDetailsCourierViewModel
import eu.bwbw.decent.ui.receiver.ReceiverViewModel
import eu.bwbw.decent.ui.receiver.details.DeliveryDetailsReceiverViewModel
import eu.bwbw.decent.ui.sender.SenderViewModel
import eu.bwbw.decent.ui.sender.addnewdelivery.AddNewDeliveryViewModel
import eu.bwbw.decent.ui.sender.details.DeliveryDetailsSenderViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.web3j.crypto.Credentials
import org.web3j.protocol.Web3j
import org.web3j.protocol.http.HttpService

@Suppress("UNCHECKED_CAST")
class ViewModelFactory private constructor(application: Application) : ViewModelProvider.NewInstanceFactory() {

    private val courierServiceContractAddress = "A193E42526F1FEA8C99AF609dcEabf30C1c29fAA"
    private val web3j = Web3j.build(
        HttpService("http://10.0.2.2:8545") // TODO move to properties
    )
    private val userDataRepository = UserDataRepository(application)
    private val courierServiceRepository = CourierServiceRepository(
        courierServiceContractAddress,
        web3j,
        userDataRepository
    )
    private val deliveryDetailsRepository = DeliveryDetailsMemoryRepository()

    private val deliveriesService = DeliveriesService(
        courierServiceRepository,
        deliveryDetailsRepository
    )

    override fun <T : ViewModel> create(modelClass: Class<T>) =
        with(modelClass) {
            when {
                isAssignableFrom(AddNewDeliveryViewModel::class.java) ->
                    AddNewDeliveryViewModel(deliveriesService)

                isAssignableFrom(SenderViewModel::class.java) ->
                    SenderViewModel(userDataRepository, deliveriesService)

                isAssignableFrom(CourierViewModel::class.java) ->
                    CourierViewModel(userDataRepository, deliveriesService)

                isAssignableFrom(ReceiverViewModel::class.java) ->
                    ReceiverViewModel(userDataRepository, deliveriesService)

                isAssignableFrom(DeliveryDetailsSenderViewModel::class.java) ->
                    DeliveryDetailsSenderViewModel(deliveriesService)

                isAssignableFrom(DeliveryDetailsCourierViewModel::class.java) ->
                    DeliveryDetailsCourierViewModel(deliveriesService)

                isAssignableFrom(DeliveryDetailsReceiverViewModel::class.java) ->
                    DeliveryDetailsReceiverViewModel(deliveriesService)

                else ->
                    throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
            }
        } as T

    init {
        CoroutineScope(Dispatchers.Main).launch {
            loadSampleData()
        }
    }

    private suspend fun loadSampleData() {
        val sampleDeliveryDetails01 = DeliveryDetails(
            "Zlecenie przewozu kota v2",
            "Duży, rudy w koszu",
            "ul. Kwiatowa 14/12, Warszawa"
        )
        val sampleDeliveryDetails02 = DeliveryDetails(
            "Zlecenie przewozu dużego dzika v2",
            "Duży, dziki w koszu",
            "ul. Andaluzyjska 11/1, Warszawa"
        )

        CourierServiceRepository(
            "A193E42526F1FEA8C99AF609dcEabf30C1c29fAA",
            web3j,
            UserDataMockRepository(
                Credentials.create("5c8b9227cd5065c7e3f6b73826b8b42e198c4497f6688e3085d5ab3a6d520e74")
            )
        ).apply {
            createDeliveryOrder(
                DeliveryOrder(
                    EthAddress("0xd59ca627Af68D29C547B91066297a7c469a7bF72"),
                    100.toBigInteger(),
                    200.toBigInteger(),
                    60 * 60 * 2,
                    deliveryDetailsRepository.save(sampleDeliveryDetails01)
                )
            )

            createDeliveryOrder(
                DeliveryOrder(
                    // EthAddress("0x63FC2aD3d021a4D7e64323529a55a9442C444dA0"),
                    EthAddress("0xFC6F167a5AB77Fe53C4308a44d6893e8F2E54131"),
                    100.toBigInteger(),
                    200.toBigInteger(),
                    60 * 60 * 4,
                    deliveryDetailsRepository.save(sampleDeliveryDetails02)
                )
            )
        }

    }

    companion object {

        @SuppressLint("StaticFieldLeak")
        @Volatile
        private var INSTANCE: ViewModelFactory? = null

        fun getInstance(application: Application? = null): ViewModelFactory {
            return INSTANCE ?: synchronized(ViewModelFactory::class.java) {
                INSTANCE ?: ViewModelFactory(application ?: throw Exception("Application parameter to ViewModelFactory.getInstance() is null"))
                    .also { INSTANCE = it }
            }
        }
    }
}