package de.patternframeworks.busash.user

import org.springframework.data.repository.CrudRepository
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface UserRepository : CrudRepository<User, Long> {
    fun findByEmail(email: String): Optional<User>

    fun comparePassword(mail: String, password: String): Boolean {
        val user = this.findByEmail(mail)
        return BCryptPasswordEncoder().matches(password, user.get().password)
    }
}