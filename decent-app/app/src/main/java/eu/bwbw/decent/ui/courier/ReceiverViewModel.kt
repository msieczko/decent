package eu.bwbw.decent.ui.courier

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class CourierViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is courier Fragment"
    }
    val text: LiveData<String> = _text
}