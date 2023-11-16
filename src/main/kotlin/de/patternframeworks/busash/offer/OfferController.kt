package de.patternframeworks.busash.offer

import de.patternframeworks.busash.auth.service.JwtTokenServiceImpl
import de.patternframeworks.busash.user.persistance.UserRepository
import de.patternframeworks.busash.user.service.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/offers")
class OfferController(
        @Autowired private val offerRepository: OfferRepository,
        private val jwtTokenService: JwtTokenServiceImpl,
        private val userRepository: UserRepository,
        private val userService: UserService
) {
    /**
     * Endpoint to retrieve all offers EXCLUDING offers of authentificated user
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
        return allOffers.filter { it.author?.id != userId }
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
    ): List<Offer>{
        val token = jwtTokenService.extractTokenFromPrefix(header)
        val userId = jwtTokenService.getUserIdFromToken(token)
        return offerRepository.findAllByAuthorId(userId)
    }

    /**
     * Endpoint to retrieve all offers of specified user
     * @param header Authorization header containing the user token
     * @return List of offers
     * @author Konstantin K.
     */
    @GetMapping("/users/{userId}")
    fun getAllMyOffers(
        @PathVariable userId: Long,
    ): List<Offer>{
        return offerRepository.findAllByAuthorId(userId)
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
     * Endpoint to delete offer
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
}