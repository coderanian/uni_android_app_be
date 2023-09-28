package de.patternframeworks.busash.user

import de.patternframeworks.busash.location.Location
import jakarta.persistence.*

@Entity
@Table(name = "users")
data class User(
        @Id @GeneratedValue(strategy = GenerationType.IDENTITY) @Column(name = "id") val id: Long,
        @Column(name = "name") val name: String,
        @Column(name = "email") val email: String,
        @Column(name = "password") val password: String,
        @ManyToOne @JoinColumn(name = "location_id") var location: Location? = null
)
