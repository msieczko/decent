package eu.bwbw.decent.ui.receiver

import eu.bwbw.decent.services.DeliveriesService
import eu.bwbw.decent.ui.common.BaseDeliveryDetailsViewModel

class DeliveryDetailsReceiverViewModel(
    private val deliveriesService: DeliveriesService
) : BaseDeliveryDetailsViewModel(deliveriesService) {

}
