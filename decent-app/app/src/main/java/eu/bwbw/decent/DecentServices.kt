package eu.bwbw.decent

import android.app.Application
import android.content.Context
import dagger.Component
import eu.bwbw.decent.services.DeliveryDetailsMemoryRepository
import javax.inject.Singleton

@Singleton
@Component(modules = [ApplicationModule::class])
interface DecentServices {
    fun context(): Context
    fun applicationContext(): Application
    fun deliveryDetailsRepository(): DeliveryDetailsMemoryRepository
}