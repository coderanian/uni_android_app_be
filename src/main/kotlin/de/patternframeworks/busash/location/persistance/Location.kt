package de.patternframeworks.busash.location.persistance

import javax.persistence.*

@Entity
@Table(name = "locations")
data class Location (
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) @Column(name = "location_id") val locationId: Long?,
    @Column(name = "latitude") val latitude: Double,
    @Column(name = "longitude") val longitude: Double
)
