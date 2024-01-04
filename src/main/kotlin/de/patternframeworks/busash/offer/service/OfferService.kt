package de.patternframeworks.busash.offer.service

import de.patternframeworks.busash.offer.persistance.Offer
import de.patternframeworks.busash.reservation.persistance.Reservation

interface OfferService {
    fun isOfferReserved(offer: Offer): Boolean
    fun isReservationActive(reservation: Reservation): Boolean
    fun getReservationEndpoint(offer: Offer): String?
}