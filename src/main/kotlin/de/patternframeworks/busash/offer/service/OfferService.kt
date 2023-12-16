package de.patternframeworks.busash.offer.service

import de.patternframeworks.busash.model.*
import de.patternframeworks.busash.offer.persistance.Offer
import de.patternframeworks.busash.reservation.persistance.Reservation
import java.util.*

interface OfferService {
    fun getSearchViewOffers(userId: Long): List<OfferDto>
    fun getMyOffers(userId: Long): List<MyOfferDto>
    fun getOffersFromUser(userId: Long): List<OfferDto>
    fun createOffer(userId: Long, offerDto: OfferDto): Offer
    fun getOfferById(offerId: Long): Optional<Offer>
    fun updateOffer(offerId: Long, offerDto: MyOfferDto, userId: Long): MyOfferDto
    fun deleteOffer(offerId: Long, sold: Boolean, userId: Long): String
    fun isOfferReserved(offer: Offer): Boolean
    fun isReservationActive(reservation: Reservation): Boolean
    fun getReservationEndpoint(offer: Offer): String?
}