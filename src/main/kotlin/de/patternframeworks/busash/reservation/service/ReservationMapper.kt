package de.patternframeworks.busash.reservation.service

import de.patternframeworks.busash.model.ReservationDto
import de.patternframeworks.busash.offer.service.OfferMapper
import de.patternframeworks.busash.reservation.persistance.Reservation
import org.mapstruct.*

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = [OfferMapper::class])
interface ReservationMapper {
    @Mapping(target = "offer", source = "item")
    fun reservationToReservationDto(reservation: Reservation): ReservationDto
}