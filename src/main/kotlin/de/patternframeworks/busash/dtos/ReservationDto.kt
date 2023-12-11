package de.patternframeworks.busash.dtos

import java.time.OffsetDateTime

class ReservationDto(
    val reservationId: Long,
    val reservationTimestamp: OffsetDateTime,
    val offer: OfferDto
)