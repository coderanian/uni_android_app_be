package de.patternframeworks.busash.model

import java.time.OffsetDateTime

class ReservationDto(
    val reservationId: Long,
    val reservationTimestamp: OffsetDateTime
)