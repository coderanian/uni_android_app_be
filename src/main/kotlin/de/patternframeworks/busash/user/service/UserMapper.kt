package de.patternframeworks.busash.user.service

import de.patternframeworks.busash.location.service.LocationMapper
import de.patternframeworks.busash.model.AuthorDto
import de.patternframeworks.busash.model.ProfileDto
import de.patternframeworks.busash.model.RegisterDto
import de.patternframeworks.busash.user.persistance.User
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.ReportingPolicy

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = [LocationMapper::class])
interface UserMapper {
    fun registerDtoToUser(user: RegisterDto): User

    @Mapping(target = "offersCount", expression = "java(user.getOffers().size())")
    fun userToProfileDto(user: User): ProfileDto

    fun userToAuthorDto(user: User): AuthorDto
}