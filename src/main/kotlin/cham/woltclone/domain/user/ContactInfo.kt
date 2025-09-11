package cham.woltclone.domain.user

import jakarta.persistence.Column
import jakarta.persistence.Embeddable
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Pattern

@Embeddable
data class ContactInfo private constructor(
    @field:NotBlank(message = "First name cannot be blank")
    @Column(name = "first_name", nullable = false)
    val firstName: String,

    @field:NotBlank(message = "Last name cannot be blank") 
    @Column(name = "last_name", nullable = false)
    val lastName: String,

    @field:NotBlank(message = "Phone number cannot be blank")
    @field:Pattern(
        regexp = "^\\+?[1-9]\\d{1,14}$",
        message = "Phone number must be in international format (e.g., +1234567890)"
    )
    @Column(name = "phone_number", nullable = false)
    val phoneNumber: String
) {
    companion object {
        private val PHONE_PATTERN = Regex("^\\+?[1-9]\\d{2,14}$")

        fun create(
            firstName: String,
            lastName: String, 
            phoneNumber: String
        ): ContactInfo {
            require(firstName.isNotBlank()) { "First name cannot be blank" }
            require(lastName.isNotBlank()) { "Last name cannot be blank" }
            require(phoneNumber.isNotBlank()) { "Phone number cannot be blank" }
            require(isValidPhoneNumber(phoneNumber)) { "Phone number must be in valid international format" }
            return ContactInfo(firstName, lastName, phoneNumber)
        }

        fun isValidPhoneNumber(phone: String): Boolean {
            return PHONE_PATTERN.matches(phone)
        }
    }

    fun getFormattedPhone(): String {
        return if (phoneNumber.startsWith("+")) phoneNumber else "+$phoneNumber"
    }
    
    fun getFullName(): String {
        return "$firstName $lastName"
    }

}
