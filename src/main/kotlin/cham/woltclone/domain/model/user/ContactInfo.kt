package cham.woltclone.domain.model.user

import jakarta.persistence.Column
import jakarta.persistence.Embeddable
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Pattern

@Embeddable
data class ContactInfo(
    @field:NotBlank(message = "Phone number cannot be blank")
    @field:Pattern(
        regexp = "^\\+?[1-9]\\d{1,14}$",
        message = "Phone number must be in international format (e.g., +1234567890)"
    )

    @Column(name = "phone_number", nullable = false)
    val phone: String
) {
    init {
        require(phone.isNotBlank()) { "Phone number cannot be blank" }
        require(isValidPhoneNumber(phone)) { "Phone number must be in valid international format" }
    }

    companion object {
        private val PHONE_PATTERN = Regex("^\\+?[1-9]\\d{2,14}$")

        fun isValidPhoneNumber(phone: String): Boolean {
            return PHONE_PATTERN.matches(phone)
        }
    }

    fun getFormattedPhone(): String {
        return if (phone.startsWith("+")) phone else "+$phone"
    }

}
