package de.patternframeworks.busash.location.persistance

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface LocationRepository: CrudRepository<Location, Long> {
}