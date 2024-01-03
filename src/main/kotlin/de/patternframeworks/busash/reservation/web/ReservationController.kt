package de.patternframeworks.busash.reservation.web

import de.patternframeworks.busash.auth.service.JwtTokenService
import de.patternframeworks.busash.error.MainException
import de.patternframeworks.busash.model.OfferDto
import de.patternframeworks.busash.reservation.service.ReservationService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/reservations")
class ReservationController(
    private val jwtTokenService: JwtTokenService,
    private val reservationService: ReservationService
) {
    /**
     * Endpoint to retrieve all reserved offers of authenticated user
     * @param header Authorization header containing the user token
     * @return List of offers
     * @author Konstantin K.
     */
    @GetMapping("")
    fun getAllReservedOffers(
        @RequestHeader(name = "Authorization") header: String,
        @RequestParam("cat") cat: String?,
        @RequestParam("typ") typ: String?
    ): ResponseEntity<List<OfferDto>> {
        val userId = jwtTokenService.getUserIdFromHeader(header)
        return ResponseEntity.ok(reservationService.getReservedOffers(userId, cat, typ))
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
        return try {
            ResponseEntity.ok(reservationService.reserveOffer(userId, id))
        } catch (e: MainException) {
            ResponseEntity(e, HttpStatus.NOT_ACCEPTABLE)
        }
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
        return try {
            ResponseEntity.ok(reservationService.cancelReservation(userId, id))
        } catch (e: MainException) {
            ResponseEntity(e, HttpStatus.NOT_ACCEPTABLE)
        }

    }
}