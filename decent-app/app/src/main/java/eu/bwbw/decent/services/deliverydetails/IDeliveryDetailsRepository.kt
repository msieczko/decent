package eu.bwbw.decent.services.deliverydetails

data class DeliveryDetails(
    val title: String,
    val description: String,
    val receiverPostalAddress: String
)

interface IDeliveryDetailsRepository {
    suspend fun save(deliveryDetails: DeliveryDetails): String
    suspend fun get(detailsHash: String): DeliveryDetails?
}
