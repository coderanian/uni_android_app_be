package de.patternframeworks.busash.offer

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/offers")
class OfferController(@Autowired private val offerRepository: OfferRepository) {

    @GetMapping("")
    fun getAllOffers(): List<Offer> =
        offerRepository.findAll().toList()
}