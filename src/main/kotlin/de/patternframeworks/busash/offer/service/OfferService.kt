package de.patternframeworks.busash.offer.service

import de.patternframeworks.busash.model.*
import de.patternframeworks.busash.offer.persistance.Offer
import de.patternframeworks.busash.reservation.persistance.Reservation
import java.util.*

interface OfferService {
    fun getSearchViewOffers(userId: Long, currentLocation: LocationDto, radius: Double, categories: String?, types: String?): List<OfferDto>
    fun getMyOffers(userId: Long): List<MyOfferDto>
    fun getOffersFromUser(userId: Long): List<OfferDto>
    fun createOffer(userId: Long, offerDto: OfferDto): Offer
    fun getOfferById(offerId: Long): Optional<Offer>
    fun updateOffer(offerId: Long, offerDto: MyOfferDto, userId: Long): MyOfferDto
    fun deleteOffer(offerId: Long, sold: Boolean, userId: Long): String
    fun isOfferReserved(offer: Offer): Boolean
    fun isReservationActive(reservation: Reservation): Boolean
    fun getReservationEndpoint(offer: Offer): String?
    fun distanceFilter(offer: Offer, currentLocation: LocationDto, radius: Double): Boolean
    fun filterForCategories(offerList: List<Offer>, categories: String?): List<Offer>
    fun filterForPriceTypes(offerList: List<Offer>, types: String?): List<Offer>
}