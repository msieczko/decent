package eu.bwbw.decent.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ReceiverViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is receiver Fragment"
    }
    val text: LiveData<String> = _text
}