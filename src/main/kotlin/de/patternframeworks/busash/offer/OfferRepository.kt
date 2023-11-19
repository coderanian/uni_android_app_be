package de.patternframeworks.busash.offer

import de.patternframeworks.busash.user.persistance.User
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface OfferRepository: CrudRepository<Offer, Long> {
    fun countByAuthorId(authorId: Long): Long
    fun findAllByAuthorId(authorId: Long): List<Offer>
    fun findAllByReservedBy(reservedBy: User): List<Offer>
}