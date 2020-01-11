package eu.bwbw.decent.ui.sender.details

import eu.bwbw.decent.services.DeliveriesService
import eu.bwbw.decent.ui.common.details.BaseDeliveryDetailsViewModel

class DeliveryDetailsSenderViewModel(
    private val deliveriesService: DeliveriesService
) : BaseDeliveryDetailsViewModel(deliveriesService) {

}
