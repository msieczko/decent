package eu.bwbw.decent

import androidx.lifecycle.ViewModel
import eu.bwbw.decent.services.CourierServiceRepository
import eu.bwbw.decent.utils.weiToString

class MainActivityViewModel(
    private val courierServiceRepository: CourierServiceRepository
) : ViewModel() {

    suspend fun getWalletBalance(): String {
        return weiToString(courierServiceRepository.getWalletBalance().toBigDecimal())
    }
}