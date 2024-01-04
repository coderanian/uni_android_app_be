package de.patternframeworks.busash.model

class ProfileDto(
    val name: String,
    val email: String,
    val transactions: Int?,
    val picture: String?,
    val location: LocationDto?,
    val newPassword: String?,
    val offersCount: Int
)