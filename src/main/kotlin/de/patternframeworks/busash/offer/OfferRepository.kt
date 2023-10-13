package de.patternframeworks.busash.offer

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface OfferRepository: CrudRepository<Offer, Long> {
}