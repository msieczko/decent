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

    fun signOut() {
        userDataRepository.clearUserData()
    }

    fun withDrawMoney() {
        viewModelScope.launch {
            courierServiceRepository.withdrawMoney()
        }
    }

    private val _walletInfo = MutableLiveData<String>().apply {
        value = "Eth address: ${userDataRepository.getCredentials().address}"
    }
    val walletInfo: LiveData<String>
        get() = _walletInfo

}
