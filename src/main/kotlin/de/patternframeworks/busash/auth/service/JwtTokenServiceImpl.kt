package de.patternframeworks.busash.auth.service

import de.patternframeworks.busash.user.persistance.User
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import org.springframework.stereotype.Service
import java.util.*

@Service
class JwtTokenServiceImpl(): JwtTokenService {
    override fun generateToken(userId: Long): String {
        val subject = userId.toString()
        val expiration = Date(System.currentTimeMillis() + 60 * 48 * 1000) // 1day

        return Jwts.builder()
            .setSubject(subject)
            .setExpiration(expiration)
            .signWith(SignatureAlgorithm.HS512, "secret")
            .compact()
    }

    override fun extractTokenFromPrefix(token: String): String {
        if (token.startsWith("Bearer ")) {
            return token.removePrefix("Bearer ")
        }
        return token
    }

    override fun getUserIdFromToken(token: String): Long {
        val userid = Jwts.parser().setSigningKey("secret").parseClaimsJws(token).body.subject
        return userid.toLong()
    }

    override fun validateToken(token: String, userDetails: User): Boolean {
        return getUserIdFromToken(token) == userDetails.id && !isTokenExpired(token)
    }

    override fun getExpirationDateFromToken(token: String): Date {
        return Jwts.parser().setSigningKey("secret").parseClaimsJws(token).body.expiration
    }

    fun isTokenExpired(token: String): Boolean {
        val expiration = getExpirationDateFromToken(token)
        return expiration.before(Date())
    }
}