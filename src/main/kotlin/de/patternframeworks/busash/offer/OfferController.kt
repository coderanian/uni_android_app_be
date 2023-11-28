package de.patternframeworks.busash.offer

import de.patternframeworks.busash.auth.service.JwtTokenServiceImpl
import de.patternframeworks.busash.user.persistance.UserRepository
import de.patternframeworks.busash.user.service.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.time.LocalDateTime

@RestController
@RequestMapping("/api/offers")
class OfferController(
        @Autowired private val offerRepository: OfferRepository,
        private val jwtTokenService: JwtTokenServiceImpl,
        private val userRepository: UserRepository,
        private val userService: UserService
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
    ): List<Offer>{
        val token = jwtTokenService.extractTokenFromPrefix(header)
        val userId = jwtTokenService.getUserIdFromToken(token)
        val allOffers = offerRepository.findAll().toList()
        return allOffers.filter { it.author?.id != userId && it.reservedBy == null}
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
    ): ResponseEntity<List<Offer>>{
        val token = jwtTokenService.extractTokenFromPrefix(header)
        val userId = jwtTokenService.getUserIdFromToken(token)
        val existingUser = userRepository.findById(userId).orElse(null) ?: return ResponseEntity(HttpStatus.NOT_FOUND)
        //Prevent user to create offers if no location has been set in the profile
        if(existingUser.location == null){
            return ResponseEntity(HttpStatus.NOT_ACCEPTABLE)
        }
        val myOffers = offerRepository.findAllByAuthorId(userId)
        return ResponseEntity(myOffers, HttpStatus.OK)
    }

    /**
     * Endpoint to retrieve all unreserved offers of specified user
     * @param header Authorization header containing the user token
     * @return List of offers
     * @author Konstantin K.
     */
    @GetMapping("/users/{userId}")
    fun getAllUserOffers(
        @PathVariable userId: Long,
    ): List<Offer>{
        val userOffers = offerRepository.findAllByAuthorId(userId)
        return userOffers.filter { it.reservedBy == null }
    }

    /**
     * Endpoint to retrieve all reserved offers of authenticated user
     * @param header Authorization header containing the user token
     * @return List of offers
     * @author Konstantin K.
     */
    @GetMapping("/my-reservations")
    fun getAllReservedOffers(
        @RequestHeader(name = "Authorization") header: String
    ): ResponseEntity<List<Offer>>{
        val token = jwtTokenService.extractTokenFromPrefix(header)
        val userId = jwtTokenService.getUserIdFromToken(token)
        val existingUser = userRepository.findById(userId).orElse(null) ?: return ResponseEntity(HttpStatus.NOT_FOUND)
        val myReservations = offerRepository.findAllByReservedBy(existingUser)
        return ResponseEntity(myReservations, HttpStatus.OK)
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
        @RequestBody offer: Offer
    ): ResponseEntity<Offer> {
        val token = jwtTokenService.extractTokenFromPrefix(header)
        val userId = jwtTokenService.getUserIdFromToken(token)
        val existingUser = userRepository.findById(userId).orElse(null) ?: return ResponseEntity(HttpStatus.NOT_FOUND)
        //Extend offer properties from input fields with author_id
        val offerWithUser = offer.copy(author = existingUser)
        val newOffer = offerRepository.save(offerWithUser)
        return ResponseEntity(newOffer, HttpStatus.CREATED)
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
        val offer = offerRepository.findById(id).orElse(null)
        return if (offer != null) ResponseEntity(offer, HttpStatus.OK)
        else ResponseEntity(HttpStatus.NOT_FOUND)
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
        @RequestBody offer: Offer
    ): ResponseEntity<Any> {
        val token = jwtTokenService.extractTokenFromPrefix(header)
        val userId = jwtTokenService.getUserIdFromToken(token)
        val existingUser = userRepository.findById(userId).orElse(null) ?: return ResponseEntity(HttpStatus.NOT_FOUND)
        val existingOffer = offerRepository.findById(id).orElse(null)
        //Ensure that user can only update their own offers
        if (existingOffer == null || existingOffer.author != existingUser) {
            return ResponseEntity(HttpStatus.NOT_FOUND)
        }
        val updatedOffer = existingOffer.copy(
                title = offer.title,
                description = offer.description,
                category = offer.category,
                quantity = offer.quantity,
                priceType = offer.priceType,
                price = offer.price,
                productPic = offer.productPic
        )
        offerRepository.save(updatedOffer)
        return ResponseEntity(updatedOffer, HttpStatus.OK)
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
        val token = jwtTokenService.extractTokenFromPrefix(header)
        val userId = jwtTokenService.getUserIdFromToken(token)
        val existingUser = userRepository.findById(userId).orElse(null) ?: return ResponseEntity(HttpStatus.NOT_FOUND)
        val offer = offerRepository.findById(id).orElse(null)
        //Ensure that user can only delete their own offers
        if (offer == null || offer.author != existingUser) {
            return ResponseEntity(HttpStatus.NOT_FOUND)
        }
        //Increment transactions counter of a user if item is sold, otherwise simply delete
        if(sold) {
            userService.updateTransactionsCnt(existingUser)
        }
        offerRepository.deleteById(id)
        return ResponseEntity("Offer deleted", HttpStatus.OK)
    }

    /**
     * Endpoint to reserve offer
     * @param header Authorization header containing the user token
     * @param id offer id as path variable in URL
     * @return server response and HTTP status code
     * @author Konstantin K.
     */
    @PutMapping("/{id}/reserve")
    fun reserveOffer(
        @RequestHeader(name = "Authorization") header: String,
        @PathVariable id: Long,
    ): ResponseEntity<Any> {
        val token = jwtTokenService.extractTokenFromPrefix(header)
        val userId = jwtTokenService.getUserIdFromToken(token)
        val existingUser = userRepository.findById(userId).orElse(null) ?: return ResponseEntity(HttpStatus.NOT_FOUND)
        val offer = offerRepository.findById(id).orElse(null)
        //Don't allow to reserve your own items or items already reserved by a user
        if (offer == null || offer.reservedBy != null || offer.author == existingUser) {
            return ResponseEntity(HttpStatus.NOT_FOUND)
        }
        val updatedOffer = offer.copy(
                reservedBy = existingUser,
                reservationTime = LocalDateTime.now()
        )
        offerRepository.save(updatedOffer)
        return ResponseEntity(updatedOffer, HttpStatus.OK)
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
        val token = jwtTokenService.extractTokenFromPrefix(header)
        val userId = jwtTokenService.getUserIdFromToken(token)
        val existingUser = userRepository.findById(userId).orElse(null) ?: return ResponseEntity(HttpStatus.NOT_FOUND)
        val offer = offerRepository.findById(id).orElse(null)
        //Don't allow to reserve your own items or items already reserved by a user
        if (offer == null || offer.reservedBy == null || offer.author == existingUser) {
            return ResponseEntity(HttpStatus.NOT_FOUND)
        }
        val updatedOffer = offer.copy(
                reservedBy = null,
                reservationTime = null
        )
        offerRepository.save(updatedOffer)
        return ResponseEntity(updatedOffer, HttpStatus.OK)
    }
}