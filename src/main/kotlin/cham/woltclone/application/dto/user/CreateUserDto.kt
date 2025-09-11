package cham.woltclone.application.dto.user

data class CreateUserDto(
    val email: String,
    val password: String,
    val firstName: String,
    val lastName: String,
    val phoneNumber: String,
    val street: String,
    val city: String,
    val postalCode: String,
    val latitude: Double? = null,
    val longitude: Double? = null
)