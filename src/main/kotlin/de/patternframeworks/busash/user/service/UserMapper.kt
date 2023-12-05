package de.patternframeworks.busash.user.service

import de.patternframeworks.busash.dtos.AuthorDto
import de.patternframeworks.busash.dtos.ProfileDto
import de.patternframeworks.busash.dtos.RegisterDto
import de.patternframeworks.busash.user.persistance.User
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.ReportingPolicy

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
interface UserMapper {
    fun registerDtoToUser(user: RegisterDto): User

    @Mapping(target = "offersCount", expression = "java(user.getOffers().size())")
    fun userToProfileDto(user: User): ProfileDto

    fun userToAuthorDto(user: User): AuthorDto
}