package de.patternframeworks.busash.user.service

import de.patternframeworks.busash.dtos.RegisterDto
import de.patternframeworks.busash.user.persistance.User
import org.mapstruct.Mapper
import org.mapstruct.ReportingPolicy

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
interface UserMapper {
    fun registerDtoToUser(user: RegisterDto): User

}