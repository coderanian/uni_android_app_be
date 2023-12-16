package de.patternframeworks.busash.reservation.service

import de.patternframeworks.busash.model.OfferDto
import de.patternframeworks.busash.model.ReservationDto

interface ReservationService {
    fun getReservedOffers(userId: Long): List<OfferDto>
    fun reserveOffer(userId: Long, offerId: Long): ReservationDto
    fun cancelReservation(userId: Long, reservationId: Long)
}