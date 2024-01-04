package de.patternframeworks.busash.reservation.service

import de.patternframeworks.busash.model.OfferDto
import de.patternframeworks.busash.model.ReservationDto
import de.patternframeworks.busash.reservation.persistance.Reservation

interface ReservationService {
    fun getReservedOffers(userId: Long, categories: String?, types: String?): List<OfferDto>
    fun reserveOffer(userId: Long, offerId: Long): ReservationDto
    fun cancelReservation(userId: Long, reservationId: Long)
    fun filterForCategories(reservationList: List<Reservation>, categories: String?): List<Reservation>
    fun filterForPriceTypes(reservationList: List<Reservation>, types: String?): List<Reservation>
}