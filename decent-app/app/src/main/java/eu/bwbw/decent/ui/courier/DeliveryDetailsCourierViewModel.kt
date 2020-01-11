package eu.bwbw.decent.ui.courier

import eu.bwbw.decent.services.DeliveriesService
import eu.bwbw.decent.ui.common.BaseDeliveryDetailsViewModel

class DeliveryDetailsCourierViewModel(
    private val deliveriesService: DeliveriesService
) : BaseDeliveryDetailsViewModel(deliveriesService) {

}
