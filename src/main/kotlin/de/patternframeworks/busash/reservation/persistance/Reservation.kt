package de.patternframeworks.busash.reservation.persistance

import de.patternframeworks.busash.offer.persistance.Offer
import de.patternframeworks.busash.user.persistance.User
import java.time.OffsetDateTime
import javax.persistence.*

@Entity
@Table(name = "reservations")
data class Reservation(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) @Column(name = "reservation_id") val reservationId: Long?,
    @ManyToOne @JoinColumn(name = "offer_id") var item: Offer,
    @ManyToOne @JoinColumn(name = "user_id") var reserved: User,
    @Column(name = "reservation_timestamp") var reservationTimestamp: OffsetDateTime
)
