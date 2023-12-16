package de.patternframeworks.busash.reservation.service

import de.patternframeworks.busash.error.MainException
import de.patternframeworks.busash.model.OfferDto
import de.patternframeworks.busash.model.ReservationDto
import de.patternframeworks.busash.offer.service.OfferService
import de.patternframeworks.busash.reservation.persistance.Reservation
import de.patternframeworks.busash.reservation.persistance.ReservationRepository
import de.patternframeworks.busash.user.service.UserService
import org.springframework.stereotype.Service
import java.time.OffsetDateTime

@Service
class ReservationServiceImpl(
    private val reservationRepository: ReservationRepository,
    private val reservationMapper: ReservationMapper,
    private val userService: UserService,
    private val offerService: OfferService
) : ReservationService {
    override fun getReservedOffers(userId: Long): List<OfferDto> {
        val resById = reservationRepository.findAllByReservedId(userId).toList()
        return resById
            .filter { offerService.isReservationActive(it) }
            .map { reservationMapper.reservationToOfferDto(it) }
    }

    override fun reserveOffer(userId: Long, offerId: Long): ReservationDto {
        val user = userService.getUserById(userId).orElse(null)
        val offer = offerService.getOfferById(offerId).orElse(null)
        //Don't allow to reserve your own items
        if (offer.author.id == userId && user != null) {
            throw MainException("Not allowed")
        }
        // items already reserved by a user
        if (offerService.isOfferReserved(offer)) {
            throw MainException("Not allowed")
        }
        val timestamp = OffsetDateTime.now().plusHours(1)
        val createdReservation = reservationRepository.save(Reservation(null, offer, user, timestamp))
        return reservationMapper.reservationToReservationDto(createdReservation)
    }

    override fun cancelReservation(userId: Long, reservationId: Long) {
        val reservation = reservationRepository.findById(reservationId).orElse(null)

        if (reservation.reserved.id != userId) {
            throw MainException("Not allowed")
        }

        val updatedReservation = reservation.copy(
            reservationId = reservation.reservationId,
            item = reservation.item,
            reserved = reservation.reserved,
            reservationTimestamp = OffsetDateTime.now()
        )
        reservationRepository.save(updatedReservation)
    }
}