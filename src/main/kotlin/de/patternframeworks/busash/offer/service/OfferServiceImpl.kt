package de.patternframeworks.busash.offer.service

import de.patternframeworks.busash.error.MainException
import de.patternframeworks.busash.location.service.LocationService
import de.patternframeworks.busash.model.LocationDto
import de.patternframeworks.busash.model.MyOfferDto
import de.patternframeworks.busash.model.OfferDto
import de.patternframeworks.busash.offer.OfferCategory
import de.patternframeworks.busash.offer.PriceType
import de.patternframeworks.busash.offer.persistance.Offer
import de.patternframeworks.busash.offer.persistance.OfferRepository
import de.patternframeworks.busash.reservation.persistance.Reservation
import de.patternframeworks.busash.user.service.UserService
import org.springframework.stereotype.Service
import java.time.OffsetDateTime
import java.util.*

@Service
class OfferServiceImpl(
    private val offerRepository: OfferRepository,
    private val offerMapper: OfferMapper,
    private val userService: UserService,
    private val locationService: LocationService
): OfferService {
    override fun getSearchViewOffers(userId: Long, currentLocation: LocationDto, radius: Double, categories: String?, types: String?): List<OfferDto> {
        val allOffers = offerRepository.findAll()
        var filteredOffers = allOffers.filter { offer ->
                offer.author.id != userId &&
                !isOfferReserved(offer) &&
                distanceFilter(offer, currentLocation, radius)
            }
        filteredOffers = filterForCategories(filteredOffers, categories)
        filteredOffers = filterForPriceTypes(filteredOffers, types)

        return  filteredOffers.map(offerMapper::offerToOfferDto).toList()
    }
    override fun getMyOffers(userId: Long): List<MyOfferDto> {
        val profile = userService.getProfileInformation(userId)
        //Prevent user to create offers if no location has been set in the profile
        if (profile.location == null) {
            throw MainException("not acceptable")
        }
        val myOffers = offerRepository.findAllByAuthorId(userId)
        return myOffers.map {offerMapper.offerToMyOfferDto(it, getReservationEndpoint(it))}
    }

    override fun getOffersFromUser(userId: Long): List<OfferDto> {
        val allOffers = offerRepository.findAllByAuthorId(userId)
        return allOffers.filter{ isOfferReserved(it) }.map(offerMapper::offerToOfferDto)
    }

    override fun createOffer(userId: Long, offerDto: OfferDto): Offer {
        val profile = userService.getUserById(userId).orElse(null) ?: throw MainException("User not found")
        return offerRepository.save(offerMapper.offerDtoToOffer(offerDto, profile))
    }

    override fun getOfferById(offerId: Long): Optional<Offer> {
        return offerRepository.findById(offerId)
    }

    override fun updateOffer(offerId: Long, offerDto: MyOfferDto, userId: Long): MyOfferDto {
        val existingOffer = offerRepository.findById(offerId).orElse(null)
        //Ensure that user can only update their own offers
        if (existingOffer == null || existingOffer.author.id != userId) {
            throw MainException("Not allowed")
        }
        val updatedOffer = existingOffer.copy(
            title = offerDto.title,
            description = offerDto.description,
            category = offerDto.category,
            quantity = offerDto.quantity,
            priceType = offerDto.priceType,
            price = offerDto.price,
            productPic = offerDto.productPic
        )
        offerRepository.save(updatedOffer)
        return offerMapper.offerToMyOfferDto(updatedOffer, offerDto.reservationEnd)
    }

    override fun deleteOffer(offerId: Long, sold: Boolean, userId: Long): String {
        val offer = offerRepository.findById(offerId).orElse(null)
        //Ensure that user can only delete their own offers
        if (offer == null || offer.author.id != userId) {
            throw MainException("not allowed")
        }
        //Increment transactions counter of a user if item is sold, otherwise simply delete
        if (sold) {
            userService.updateTransactionsCnt(userId)
        }
        offerRepository.deleteById(offerId)
        return "Delete successful"
    }

    override fun isOfferReserved(offer: Offer): Boolean {
        if (offer.reservations.isNullOrEmpty()) {
            return false
        }
        val lastReservation = offer.reservations.last()
        return isReservationActive(lastReservation)
    }

    override fun isReservationActive(reservation: Reservation): Boolean {
        return reservation.reservationTimestamp > OffsetDateTime.now()
    }

    override fun getReservationEndpoint(offer: Offer): String? {
        if (offer.reservations.isNullOrEmpty() || offer.reservations.last().reservationTimestamp < OffsetDateTime.now()) {
            return null
        }
        return offer.reservations.last().reservationTimestamp.toString()
    }

    override fun distanceFilter(offer: Offer, currentLocation: LocationDto, radius: Double): Boolean {
        val offerLocation = offer.author.location ?: throw MainException("Location not found")
        val distance = locationService.calculateDistance(offerLocation.latitude, offerLocation.longitude, currentLocation.latitude, currentLocation.longitude)
        return distance <= radius
    }

    override fun filterForCategories(offerList: List<Offer>, categories: String?): List<Offer> {
        var updatedList = offerList
        if (!categories.isNullOrEmpty()) {
            val category: List<OfferCategory> = categories.split(",").map { enumValueOf(it) }
            if (category.isNotEmpty()) {
                updatedList = offerList.filter { category.contains(it.category) }
            }
        }
        return updatedList
    }

    override fun filterForPriceTypes(offerList: List<Offer>, types: String?): List<Offer> {
        var updatedList = offerList
        if (!types.isNullOrEmpty()) {
            val type: List<PriceType> = types.split(",").map { enumValueOf(it) }
            if (type.isNotEmpty()) {
                updatedList = offerList.filter { type.contains(it.priceType) }
            }
        }
        return updatedList
    }
}