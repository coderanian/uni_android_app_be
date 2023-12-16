package de.patternframeworks.busash.location.service

import de.patternframeworks.busash.location.persistance.Location
import de.patternframeworks.busash.model.LocationDto
import org.mapstruct.Mapper
import org.mapstruct.ReportingPolicy

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
interface LocationMapper {
    fun dtoToLocation(locationDto: LocationDto): Location
}