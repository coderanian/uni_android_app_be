package de.patternframeworks.busash.offer.web

import de.patternframeworks.busash.auth.service.JwtTokenService
import de.patternframeworks.busash.error.MainException
import de.patternframeworks.busash.model.MyOfferDto
import de.patternframeworks.busash.model.OfferDto
import de.patternframeworks.busash.offer.persistance.Offer
import de.patternframeworks.busash.offer.service.OfferService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/offers")
class OfferController(
    private val jwtTokenService: JwtTokenService,
    private val offerService: OfferService
) {
    /**
     * Endpoint to retrieve all offers EXCLUDING offers of authentificated user or RESERVED
     * @param header Authorization header containing the user token
     * @return List of offers
     * @author Konstantin K.
     */
    @GetMapping("")
    fun getAllOffers(
        @RequestHeader(name = "Authorization") header: String
    ): ResponseEntity<List<OfferDto>>{
        val userId = jwtTokenService.getUserIdFromHeader(header)
        return ResponseEntity.ok(offerService.getSearchViewOffers(userId))
    }
    /**
     * Endpoint to retrieve all offers of authentificated user
     * @param header Authorization header containing the user token
     * @return List of offers
     * @author Konstantin K.
     */
    @GetMapping("/my-offers")
    fun getAllMyOffers(
        @RequestHeader(name = "Authorization") header: String
    ): ResponseEntity<Any>{
        val userId = jwtTokenService.getUserIdFromHeader(header)
        return try {
            ResponseEntity.ok(offerService.getMyOffers(userId))
        } catch (e: MainException) {
            ResponseEntity(HttpStatus.NOT_ACCEPTABLE)
        }
    }

    /**
     * Endpoint to retrieve all unreserved offers of specified user
     * @param header Authorization header containing the user token
     * @return List of offers
     * @author Konstantin K.
     */
    @GetMapping("/users/{userId}")
    fun getOffersFromUser(
        @PathVariable userId: Long,
    ): ResponseEntity<List<OfferDto>>{
        return ResponseEntity.ok(offerService.getOffersFromUser(userId))
    }

    /**
     * Endpoint to create new offer
     * @param header Authorization header containing the user token
     * @param offer Offer details provided in request body
     * @return ResponseEntity with the created offer and HTTP status code
     * @author Konstantin K.
     */
    @PostMapping("")
    fun createOffer(
        @RequestHeader(name = "Authorization") header: String,
        @RequestBody offer: OfferDto
    ): ResponseEntity<Offer> {
        val userId = jwtTokenService.getUserIdFromHeader(header)
        return ResponseEntity(offerService.createOffer(userId, offer), HttpStatus.CREATED)
    }

    /**
     * Endpoint to get offer details
     * @param id offer id as path variable in URL
     * @return ResponseEntity with the offer and HTTP status code
     * @author Konstantin K.
     */
    @GetMapping("/{id}")
    fun getOffer(
        @PathVariable id: Long
    ): ResponseEntity<Offer>{
        val offer = offerService.getOfferById(id).orElse(null) ?: return ResponseEntity(HttpStatus.NOT_FOUND)
        return ResponseEntity.ok(offer)
    }

    /**
     * Endpoint to update offer
     * @param header Authorization header containing the user token
     * @param id offer id as path variable in URL
     * @return ResponseEntity with the offer and HTTP status code
     * @author Konstantin K.
     */
    @PutMapping("/{id}")
    fun updateOffer(
        @RequestHeader(name = "Authorization") header: String,
        @PathVariable id: Long,
        @RequestBody offer: MyOfferDto
    ): ResponseEntity<MyOfferDto> {
        val userId = jwtTokenService.getUserIdFromHeader(header)
        return ResponseEntity.ok(offerService.updateOffer(id, offer, userId))
    }

    /**
     * Endpoint to delete offer
     * @param header Authorization header containing the user token
     * @param id offer id as path variable in URL
     * @return server response and HTTP status code
     * @author Konstantin K.
     */
    @DeleteMapping("/{id}")
    fun deleteOffer(
        @RequestHeader(name = "Authorization") header: String,
        @PathVariable id: Long,
        @RequestParam sold: Boolean
    ): ResponseEntity<Any> {
        val userId = jwtTokenService.getUserIdFromHeader(header)
        return ResponseEntity.ok(offerService.deleteOffer(id, sold, userId))
    }

}