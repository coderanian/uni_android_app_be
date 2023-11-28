package de.patternframeworks.busash.offer

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/offers")
class OfferController(@Autowired private val offerRepository: OfferRepository) {

    @GetMapping("")
    fun getAllOffers(): List<Offer> =
        offerRepository.findAll().toList()

    /**
     * Returns row count from offer table with corresponding user ID
     * Used to portray amount of active items offered by user without the need to cache this info
     * @author Konstantin K.
     * @param authorId - user ID
     * @return offer count
     */
    @GetMapping("/{id}")
    fun getOffersCountByUserId(@PathVariable("id") authorId: Long): Number {
        return offerRepository.countByAuthorId(authorId)
    }
}