package eu.bwbw.decent.ui.tools

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import eu.bwbw.decent.services.CourierServiceRepository
import eu.bwbw.decent.services.userdata.IUserDataRepository
import kotlinx.coroutines.launch

class ToolsViewModel(
    private val courierServiceRepository: CourierServiceRepository,
    private val userDataRepository: IUserDataRepository
) : ViewModel() {

    init {
        updateBalance()
    }

    fun signOut() {
        userDataRepository.clearUserData()
    }

    fun withdrawMoney() {
        viewModelScope.launch {
            courierServiceRepository.withdrawMoney()
        }
    }

    fun updateBalance() {
        viewModelScope.launch {
            _balance.value = "Pending withdrawal: ${courierServiceRepository.getWithdrawal().toString(10)}"
        }
    }

    private val _balance = MutableLiveData<String>()
    val balance: LiveData<String>
        get() = _balance

}
