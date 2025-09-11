package cham.woltclone.application.dto.user

import cham.woltclone.domain.user.Role
import java.time.LocalDateTime

data class UserDto(
    val id: Long,
    val email: String,
    val role: Role,
    val firstName: String,
    val lastName: String,
    val phoneNumber: String,
    val street: String,
    val city: String,
    val postalCode: String,
    val latitude: Double?,
    val longitude: Double?,
    val isActive: Boolean,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime
)