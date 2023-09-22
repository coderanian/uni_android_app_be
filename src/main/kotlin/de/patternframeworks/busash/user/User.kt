package de.patternframeworks.busash.user

import jakarta.persistence.*

@Entity
@Table(name = "users")
class User(
        @Id @GeneratedValue(strategy = GenerationType.IDENTITY) @Column(name = "id") val id: Long,
        @Column(name = "name") val name: String,
        @Column(name = "email") val email: String,
        @Column(name = "password") val password: String
)
