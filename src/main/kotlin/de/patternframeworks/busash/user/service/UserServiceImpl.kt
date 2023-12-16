package de.patternframeworks.busash.user.service

import de.patternframeworks.busash.error.MainException
import de.patternframeworks.busash.location.service.LocationService
import de.patternframeworks.busash.model.ProfileDto
import de.patternframeworks.busash.model.RegisterDto
import de.patternframeworks.busash.offer.persistance.OfferRepository
import de.patternframeworks.busash.user.persistance.User
import de.patternframeworks.busash.user.persistance.UserRepository
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service
import java.util.*

@Service
class UserServiceImpl(
        private val userMapper: UserMapper,
        private val userRepository: UserRepository,
        private val locationService: LocationService
) : UserService {

    override fun getProfileInformation(userId: Long): ProfileDto {
        val user = getUserById(userId).orElse(null) ?: throw MainException("User not found")
        return userMapper.userToProfileDto(user)
    }

    override fun getUserById(userId: Long): Optional<User> {
        return userRepository.findById(userId)
    }

    override fun updateProfile(userId: Long, profileDto: ProfileDto): ProfileDto {
        val existingUser = userRepository.findById(userId).orElse(null) ?: throw MainException("User not found")
        //Prevent email from being not unique
        if (profileDto.email != existingUser.email) {
            val emailExists = userRepository.findByEmail(profileDto.email)
            if (emailExists.isPresent) {
                throw MainException("E-Mail already exists")
            }
        }
        //Save new location only if provided by user, otherwise null
        val updatedLocation = profileDto.location?.let {
            locationService.saveLocationOrGetExisting(it)
        }
        val updatedUser = existingUser.copy(
            name = profileDto.name,
            email = profileDto.email,
            picture = profileDto.picture ?: existingUser.picture, //WHY IS IT RESET IF NOT PROVIDED IN PUT?
            password = profileDto.newPassword ?: existingUser.password,
            location = updatedLocation ?: existingUser.location
        )
        userRepository.save(updatedUser)
        return userMapper.userToProfileDto(updatedUser)
    }

    override fun saveUser(user: RegisterDto): User {
        val newUser = userMapper.registerDtoToUser(user)
        return newUser
    }

    override fun comparePassword(pw1: String, pw2: String): Boolean {
        return BCryptPasswordEncoder().matches(pw1, pw2)
    }

    /**
     * Increment transactions count of a user
     * @param user user
     * @return void
     * @author Konstantin K.
     */
    override fun updateTransactionsCnt(userId: Long) {
        val user = userRepository.findById(userId).orElse(null)
        val updatedUser = user.copy(transactions = (user.transactions ?: 0) + 1)
        userRepository.save(updatedUser)
    }

}