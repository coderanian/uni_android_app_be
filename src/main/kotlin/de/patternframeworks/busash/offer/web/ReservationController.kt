package de.patternframeworks.busash.offer.web

import de.patternframeworks.busash.auth.service.JwtTokenService
import de.patternframeworks.busash.offer.persistance.OfferRepository
import de.patternframeworks.busash.offer.persistance.Reservation
import de.patternframeworks.busash.offer.persistance.ReservationRepository
import de.patternframeworks.busash.offer.service.OfferMapper
import de.patternframeworks.busash.offer.service.OfferService
import de.patternframeworks.busash.user.persistance.UserRepository
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.time.OffsetDateTime

@RestController
@RequestMapping("/api/reservations")
class ReservationController(
    private val offerRepository: OfferRepository,
    private val reservationRepository: ReservationRepository,
    private val userRepository: UserRepository,
    private val jwtTokenService: JwtTokenService,
    private val offerMapper: OfferMapper,
    private val offerService: OfferService
) {
    /**
     * Endpoint to retrieve all reserved offers of authenticated user
     * @param header Authorization header containing the user token
     * @return List of offers
     * @author Konstantin K.
     */
    @GetMapping("")
    fun getAllReservedOffers(
        @RequestHeader(name = "Authorization") header: String
    ): Any {
        val userId = jwtTokenService.getUserIdFromHeader(header)
        val resById = reservationRepository.findAllByReservedId(userId).toList()

        val myReservations = resById
            .filter { offerService.isReservationActive(it) }
            .map { offerMapper.reservationToReservationDto(it) }

        return ResponseEntity(myReservations, HttpStatus.OK)
    }
    /**
     * Endpoint to reserve offer
     * @param header Authorization header containing the user token
     * @param id offer id as path variable in URL
     * @return server response and HTTP status code
     * @author Konstantin K.
     */
    @PostMapping("/{id}")
    fun reserveOffer(
        @RequestHeader(name = "Authorization") header: String,
        @PathVariable id: Long,
    ): ResponseEntity<Any> {
        val userId = jwtTokenService.getUserIdFromHeader(header)
        val user = userRepository.findById(userId).orElse(null)
        val offer = offerRepository.findById(id).orElse(null) ?: return ResponseEntity(HttpStatus.NOT_FOUND)
        //Don't allow to reserve your own items
        if (offer.author.id == userId && user != null) {
            return ResponseEntity(HttpStatus.FORBIDDEN)
        }
        // items already reserved by a user
        if (offerService.isOfferReserved(offer)) {
            return ResponseEntity("Offer is already reserved", HttpStatus.FORBIDDEN)
        }
        val timestamp = OffsetDateTime.now().plusHours(1)
        reservationRepository.save(Reservation(null, offer, user, timestamp))
        return ResponseEntity(timestamp, HttpStatus.OK)
    }

    /**
     * Endpoint to unreserve offer
     * @param header Authorization header containing the user token
     * @param id offer id as path variable in URL
     * @return server response and HTTP status code
     * @author Konstantin K.
     */
    @PutMapping("/{id}/unreserve")
    fun unreserveOffer(
        @RequestHeader(name = "Authorization") header: String,
        @PathVariable id: Long,
    ): ResponseEntity<Any> {
        val userId = jwtTokenService.getUserIdFromHeader(header)
        val reservation = reservationRepository.findById(id).orElse(null)

        if (reservation.reserved.id != userId) {
            return ResponseEntity(HttpStatus.FORBIDDEN)
        }

        val updatedReservation = reservation.copy(
            reservationId = reservation.reservationId,
            item = reservation.item,
            reserved = reservation.reserved,
            reservationTimestamp = OffsetDateTime.now()
        )
        reservationRepository.save(updatedReservation)
        return ResponseEntity(null, HttpStatus.OK)

    }
}