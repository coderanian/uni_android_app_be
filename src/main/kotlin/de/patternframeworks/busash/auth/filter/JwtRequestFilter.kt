package de.patternframeworks.busash.auth.filter

import de.patternframeworks.busash.auth.service.JwtTokenService
import de.patternframeworks.busash.error.MainException
import de.patternframeworks.busash.user.persistance.UserRepository
import io.jsonwebtoken.ExpiredJwtException
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
class JwtRequestFilter(
    private val jwtTokenService: JwtTokenService,
    private val userRepository: UserRepository
) : OncePerRequestFilter() {
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val token = extractTokenFromRequest(request)

        if (token != null) {
            try {
                val userId = jwtTokenService.getUserIdFromToken(token)
                val userDetails = userRepository.findById(userId)

                if (userDetails.isPresent && jwtTokenService.validateToken(token, userDetails.get())) {
                    val authentication = PreAuthenticatedAuthenticationToken(userId, null, emptyList())
                    authentication.details = WebAuthenticationDetailsSource().buildDetails(request)
                    SecurityContextHolder.getContext().authentication = authentication
                }
            } catch (e: ExpiredJwtException) {
                throw MainException("not authorized")
            }
        }

        filterChain.doFilter(request, response)
    }

    private fun extractTokenFromRequest(request: HttpServletRequest): String? {
        val authorizationHeader = request.getHeader("Authorization")
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            return authorizationHeader.removePrefix("Bearer ")
        }
        return null
    }
}