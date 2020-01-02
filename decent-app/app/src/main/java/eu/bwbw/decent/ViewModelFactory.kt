package eu.bwbw.decent

import android.annotation.SuppressLint
import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import eu.bwbw.decent.ui.sender.AddNewDeliveryViewModel
import eu.bwbw.decent.ui.common.DeliveryDetailsViewModel
import eu.bwbw.decent.ui.courier.CourierViewModel
import eu.bwbw.decent.ui.receiver.ReceiverViewModel
import eu.bwbw.decent.ui.sender.SenderViewModel

class ViewModelFactory private constructor() : ViewModelProvider.NewInstanceFactory() {

    private val deliveriesRepository = DeliveriesRepository()

    override fun <T : ViewModel> create(modelClass: Class<T>) =
        with(modelClass) {
            when {
                isAssignableFrom(AddNewDeliveryViewModel::class.java) ->
                    AddNewDeliveryViewModel(deliveriesRepository)
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
        @Volatile private var INSTANCE: ViewModelFactory? = null

        fun getInstance(application: Application) =
            INSTANCE ?: synchronized(ViewModelFactory::class.java) {
                INSTANCE ?: ViewModelFactory()
                    //Injection.provideDeliveriesRepository(application.applicationContext)) TODO https://github.com/android/architecture-samples/blob/todo-mvvm-live-kotlin/todoapp/app/src/main/java/com/example/android/architecture/blueprints/todoapp/ViewModelFactory.kt
                    .also { INSTANCE = it }
            }
    }
}