package eu.bwbw.decent.ui.sender

import eu.bwbw.decent.services.DeliveriesService
import eu.bwbw.decent.ui.common.BaseDeliveryDetailsViewModel

class DeliveryDetailsSenderViewModel(
    private val deliveriesService: DeliveriesService
) : BaseDeliveryDetailsViewModel(deliveriesService) {

}
