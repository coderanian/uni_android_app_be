package de.patternframeworks.busash.offer.service

import de.patternframeworks.busash.model.MyOfferDto
import de.patternframeworks.busash.model.OfferDto
import de.patternframeworks.busash.model.ReservationDto
import de.patternframeworks.busash.offer.persistance.Offer
import de.patternframeworks.busash.offer.persistance.Reservation
import de.patternframeworks.busash.user.service.UserMapper
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.ReportingPolicy

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = [UserMapper::class])
interface OfferMapper {

    fun offerToOfferDto(offer: Offer): OfferDto

    @Mapping(target = "reservationEnd", source = "resend")
    fun offerToMyOfferDto(offer: Offer, resend: String?): MyOfferDto

    @Mapping(target = "offer", source = "item")
    fun reservationToReservationDto(reservation: Reservation): ReservationDto

}