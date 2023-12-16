package de.patternframeworks.busash.user.service

import de.patternframeworks.busash.model.ProfileDto
import de.patternframeworks.busash.model.RegisterDto
import de.patternframeworks.busash.user.persistance.User
import java.util.*

interface UserService {
    fun getProfileInformation(userId: Long): ProfileDto
    fun getUserById(userId: Long): Optional<User>
    fun updateProfile(userId: Long, profileDto: ProfileDto): ProfileDto
    fun saveUser(user: RegisterDto): User
    fun comparePassword(pw1: String, pw2: String): Boolean
    fun updateTransactionsCnt(userId: Long)
}