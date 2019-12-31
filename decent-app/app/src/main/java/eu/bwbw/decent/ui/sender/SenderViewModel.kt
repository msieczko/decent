package eu.bwbw.decent.ui.sender

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SenderViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is sender Fragment"
    }
    val text: LiveData<String> = _text
}