package de.patternframeworks.busash.auth.service

import de.patternframeworks.busash.model.AuthInfoDto
import de.patternframeworks.busash.model.LoginDto
import de.patternframeworks.busash.model.RegisterDto
import de.patternframeworks.busash.error.MainException
import de.patternframeworks.busash.user.service.UserMapper
import de.patternframeworks.busash.user.persistance.UserRepository
import org.springframework.stereotype.Service

@Service
class AuthenticationServiceImpl(
    private val userRepository: UserRepository,
    private val jwtTokenService: JwtTokenService,
    private val userMapper: UserMapper
): AuthenticationService {
    override fun authenticateUser(loginDto: LoginDto): AuthInfoDto {
        val user = userRepository
            .findByEmail(loginDto.email)
            .orElseThrow { MainException("E-Mail or Password doesn't match") }
        if (user.password == loginDto.password) {
            val token = jwtTokenService.generateToken(user.id)
            return AuthInfoDto(user.id, token)
        } else {
            throw MainException("E-Mail or Password doesn't match")
        }
    }

    override fun registerUser(registerDto: RegisterDto): AuthInfoDto {
        val emailExists = userRepository
            .findByEmail(registerDto.email)
        if (emailExists.isPresent) {
            throw MainException("E-Mail already exists")
        } else {
            val createdUser = userRepository.save(userMapper.registerDtoToUser(registerDto))
            return AuthInfoDto(createdUser.id, jwtTokenService.generateToken(createdUser.id))
        }
    }
}