package de.patternframeworks.busash.model

import de.patternframeworks.busash.location.persistance.Location

class ProfileDto(
    val name: String,
    val email: String,
    val transactions: Int,
    val picture: String?,
    val location: Location?,
    val newPassword: String?,
    val offersCount: Int
)