package de.patternframeworks.busash.auth.service

import de.patternframeworks.busash.user.persistance.User
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.time.OffsetDateTime
import java.util.*

@Service
class JwtTokenServiceImpl(@Value("jwt.secret") private val secret: String): JwtTokenService {
    override fun generateToken(userId: Long): String {
        val subject = userId.toString()
        val expiration = OffsetDateTime.now().plusDays(7)

        return Jwts.builder()
            .setSubject(subject)
            .setIssuedAt(Date())
            .setExpiration(Date.from(expiration.toInstant()))
            .signWith(SignatureAlgorithm.HS512, secret)
            .compact()
    }

    override fun extractTokenFromPrefix(token: String): String {
        if (token.startsWith("Bearer ")) {
            return token.removePrefix("Bearer ")
        }
        return token
    }

    override fun getUserIdFromToken(token: String): Long {
        val userid = Jwts.parser().setSigningKey(secret).parseClaimsJws(token).body.subject
        return userid.toLong()
    }

    override fun validateToken(token: String, userDetails: User): Boolean {
        return getUserIdFromToken(token) == userDetails.id && !isTokenExpired(token)
    }

    override fun getExpirationDateFromToken(token: String): Date {
        return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).body.expiration
    }

    override fun getUserIdFromHeader(token: String): Long {
        return getUserIdFromToken(extractTokenFromPrefix(token))
    }

    fun isTokenExpired(token: String): Boolean {
        val expiration = getExpirationDateFromToken(token)
        return expiration.before(Date())
    }
}