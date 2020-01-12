package eu.bwbw.decent.ui.common

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import eu.bwbw.decent.domain.Delivery
import eu.bwbw.decent.services.IUserDataRepository
import kotlinx.coroutines.launch
import java.math.BigInteger

abstract class BaseDeliveriesViewModel(
    private val userDataRepository: IUserDataRepository
) : ViewModel() {
    internal var deliveries: ArrayList<Delivery> = ArrayList()

    protected val _deliveriesUpdated = MutableLiveData<Boolean>()
    val deliveriesUpdated: LiveData<Boolean>
        get() = _deliveriesUpdated

    protected val _isLoading = MutableLiveData<Boolean>(false)
    val isLoading: LiveData<Boolean>
        get() = _isLoading

    abstract suspend fun getDeliveries(): List<Delivery>

    fun updateDeliveries() {
        viewModelScope.launch {
            getDeliveries()
            _deliveriesUpdated.value = true
        }
    }

    protected fun removeDelivery(deliveryId: BigInteger) {
        deliveries.removeAll { delivery -> delivery.id == deliveryId }
        _deliveriesUpdated.value = true
    }

    fun isUserKeyPresent() = userDataRepository.isUserKeyPresent()
}
