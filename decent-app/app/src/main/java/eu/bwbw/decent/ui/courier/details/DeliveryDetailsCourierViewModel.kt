package eu.bwbw.decent.ui.courier.details

import eu.bwbw.decent.services.DeliveriesService
import eu.bwbw.decent.ui.common.details.BaseDeliveryDetailsViewModel

class DeliveryDetailsCourierViewModel(
    private val deliveriesService: DeliveriesService
) : BaseDeliveryDetailsViewModel(deliveriesService) {

}
