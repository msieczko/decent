package eu.bwbw.decent

import android.app.Application

class DecentApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        ViewModelFactory.getInstance(this)
    }

}