package de.patternframeworks.busash.offer.persistance

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface ReservationRepository: CrudRepository<Reservation, Long> {
    fun findAllByReservedId(userId: Long): List<Reservation>
}