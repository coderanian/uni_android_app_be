package de.patternframeworks.busash.user.service

import de.patternframeworks.busash.dtos.RegisterDto
import de.patternframeworks.busash.user.persistance.User

interface UserService {
 /*   fun getUserInformation(email: String): AuthInfoDto*/
    fun saveUser(user: RegisterDto): User
   /* fun findByEmail(email: String): User?*/
    fun comparePassword(pw1: String, pw2: String): Boolean
}