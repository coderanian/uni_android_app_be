package de.patternframeworks.busash.reservation.service

import de.patternframeworks.busash.model.OfferDto
import de.patternframeworks.busash.model.ReservationDto
import de.patternframeworks.busash.offer.service.OfferMapper
import de.patternframeworks.busash.reservation.persistance.Reservation
import org.mapstruct.*

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = [OfferMapper::class])
interface ReservationMapper {

    fun reservationToReservationDto(reservation: Reservation): ReservationDto

    @Mapping(target = "offerId", source = "item.offerId")
    @Mapping(target = "title", source = "item.title")
    @Mapping(target = "description", source = "item.description")
    @Mapping(target = "category", source = "item.category")
    @Mapping(target = "quantity", source = "item.quantity")
    @Mapping(target = "priceType", source = "item.priceType")
    @Mapping(target = "price", source = "item.price")
    @Mapping(target = "productPic", source = "item.productPic")
    @Mapping(target = "author", source = "item.author")
    @Mapping(target = "reservation", expression = "java(reservationToReservationDto(reservation))")
    fun reservationToOfferDto(reservation: Reservation): OfferDto
}