package de.patternframeworks.busash.offer.service

import de.patternframeworks.busash.dtos.OfferDto
import de.patternframeworks.busash.dtos.ReservationDto
import de.patternframeworks.busash.offer.persistance.Offer
import de.patternframeworks.busash.offer.persistance.Reservation
import org.mapstruct.Mapper
import org.mapstruct.ReportingPolicy

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
interface OfferMapper {
    fun offerToOfferDto(offer: Offer): OfferDto

    fun reservationToReservationDto(reservation: Reservation): ReservationDto
}