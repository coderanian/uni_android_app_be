package de.patternframeworks.busash.user.service

import de.patternframeworks.busash.dtos.RegisterDto
import de.patternframeworks.busash.offer.OfferRepository
import de.patternframeworks.busash.user.persistance.User
import de.patternframeworks.busash.user.persistance.UserRepository
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service

@Service
class UserServiceImpl(
        private val userMapper: UserMapper,
        private val userRepository: UserRepository,
        private val offerRepository: OfferRepository
) : UserService {

    /*override fun getUserInformation(email: String): AuthInfoDto {
        val user = userRepository.findByEmail(email)
            ?: throw UsernameNotFoundException("Benutzer nicht gefunden: $email")
        return userMapper.userToAuthInfo(user)
    }*/

    override fun saveUser(user: RegisterDto): User {
        val newUser = userMapper.registerDtoToUser(user)
        return newUser
    }

  /*  override fun findByEmail(email: String): User? {
        return userRepository.findByEmail(email)
    }*/

    override fun comparePassword(pw1: String, pw2: String): Boolean {
        return BCryptPasswordEncoder().matches(pw1, pw2)
    }

    /**
     * Increment transactions count of a user
     * @param user user
     * @return void
     * @author Konstantin K.
     */
    override fun updateTransactionsCnt(user: User) {
        val updatedUser = user.copy(transactions = (user.transactions ?: 0) + 1)
        userRepository.save(updatedUser)
        println("Transactions updated successfully.")
    }

    override fun getOfferCntByUser(userId: Long): Long {
        return offerRepository.countByAuthorId(userId)
    }
}