package de.patternframeworks.busash.offer.service

import de.patternframeworks.busash.offer.persistance.Offer
import de.patternframeworks.busash.reservation.persistance.Reservation
import org.springframework.stereotype.Service
import java.time.OffsetDateTime

@Service
class OfferServiceImpl : OfferService {
    override fun isOfferReserved(offer: Offer): Boolean {
        if (offer.reservations.isEmpty()) {
            return false
        }
        val lastReservation = offer.reservations.last()
        return lastReservation.reservationTimestamp > OffsetDateTime.now()
    }

    override fun isReservationActive(reservation: Reservation): Boolean {
        return reservation.reservationTimestamp > OffsetDateTime.now()
    }

    override fun getReservationEndpoint(offer: Offer): String? {
        if (offer.reservations.isEmpty() || offer.reservations.last().reservationTimestamp < OffsetDateTime.now()) {
            return null
        }
        return offer.reservations.last().reservationTimestamp.toString()
    }
}