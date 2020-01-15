package eu.bwbw.decent

import android.app.Application
import org.bouncycastle.jce.provider.BouncyCastleProvider
import java.security.Provider
import java.security.Security

class DecentApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        val provider: Provider = Security.getProvider(BouncyCastleProvider.PROVIDER_NAME)
            ?: throw RuntimeException("Error invalid security provider")
        if (provider.javaClass.equals(BouncyCastleProvider::class.java)) {
            throw RuntimeException("Error invalid security provider")
        }

        Security.removeProvider(BouncyCastleProvider.PROVIDER_NAME)
        Security.insertProviderAt(BouncyCastleProvider(), 1)

        ViewModelFactory.getInstance(this)
    }

}