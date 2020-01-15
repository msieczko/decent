package eu.bwbw.decent.services.deliverydetails

import com.google.gson.Gson
import eu.bwbw.decent.domain.DeliveryDetailsDto
import eu.bwbw.decent.domain.DeliveryDetailsHash
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Request.Builder
import okhttp3.RequestBody.Companion.toRequestBody


class DeliveryDetailsBackendRepository(
    private val backendUrl: String
) : IDeliveryDetailsRepository {
    var client = OkHttpClient()
    val gson = Gson()

    override suspend fun save(deliveryDetails: DeliveryDetails): String {
        val detailsJson = gson.toJson(deliveryDetails)

        val body = detailsJson.toRequestBody()
        val request: Request = Builder()
            .url(backendUrl + DELIVERY_DETAILS_ENDPOINT)
            .post(body)
            .build()

        return withContext(Dispatchers.IO) {
            val response = client.newCall(request).execute()
            if (response.code != 201) {
                throw Exception("server did not save delivery details")
            }
            response.body?.string() ?: throw Exception("no hash response from server")
            val deliveryDetailsHash =
                gson.fromJson<DeliveryDetailsHash>(response.body?.string(), DeliveryDetailsHash::class.java)
            deliveryDetailsHash.detailsHash
        }
    }

    override suspend fun get(detailsHash: String): DeliveryDetails? {
        val request: Request = Builder()
            .url(backendUrl + DELIVERY_DETAILS_ENDPOINT + detailsHash)
            .build()

        return withContext(Dispatchers.IO) {
            val response = client.newCall(request).execute()
            when (response.code) {
                201 -> throw Exception("server did not return delivery details")
                404 -> throw Exception("delivery details not found")
                else -> {
                    val deliveryDetailsDto: DeliveryDetailsDto =
                        gson.fromJson<DeliveryDetailsDto>(response.body?.string(), DeliveryDetailsDto::class.java)
                    DeliveryDetails(
                        deliveryDetailsDto.title,
                        deliveryDetailsDto.description,
                        deliveryDetailsDto.receiverPostalAddress
                    )

                }
            }
        }

    }

    companion object {
        const val DELIVERY_DETAILS_ENDPOINT = "/details/"
    }

}