package eu.bwbw.decent.ui.receiver.details

import eu.bwbw.decent.services.DeliveriesService
import eu.bwbw.decent.ui.common.details.BaseDeliveryDetailsViewModel

class DeliveryDetailsReceiverViewModel(
    private val deliveriesService: DeliveriesService
) : BaseDeliveryDetailsViewModel(deliveriesService) {

}
