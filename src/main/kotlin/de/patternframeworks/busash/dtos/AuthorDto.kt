package de.patternframeworks.busash.dtos

class AuthorDto(
    val id: Long,
    val name: String,
    val email: String,
    val picture: String?,
    val transactions: Int
)