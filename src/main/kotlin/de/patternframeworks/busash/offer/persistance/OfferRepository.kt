package de.patternframeworks.busash.offer.persistance

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface OfferRepository: CrudRepository<Offer, Long> {
    fun findAllByAuthorId(authorId: Long): List<Offer>
}