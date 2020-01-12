package eu.bwbw.decent.services

import com.google.gson.Gson
import org.web3j.crypto.Hash.sha3String
import javax.inject.Inject

class DeliveryDetailsMemoryRepository @Inject constructor(): IDeliveryDetailsRepository {
    private val details = HashMap<String, DeliveryDetails>()
    private val gson = Gson()

    override suspend fun save(deliveryDetails: DeliveryDetails): String {
        val serializedDetails = gson.toJson(deliveryDetails)
        val detailsHash = sha3String(serializedDetails)
        details[detailsHash] = deliveryDetails
        return detailsHash
    }

    override suspend fun get(detailsHash: String): DeliveryDetails? {
        return details[detailsHash]
    }
}