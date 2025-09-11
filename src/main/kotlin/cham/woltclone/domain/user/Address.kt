package cham.woltclone.domain.user

import jakarta.persistence.Column
import jakarta.persistence.Embeddable
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Pattern
import jakarta.validation.constraints.Size

@Embeddable
data class Address private constructor(
    @field:NotBlank(message = "Street address cannot be blank")
    @field:Size(max = 255, message = "Street address cannot exceed 255 characters")
    @Column(name = "street_address", nullable = false)
    val street: String,

    @field:NotBlank(message = "City cannot be blank")
    @field:Size(max = 100, message = "City name cannot exceed 100 characters")
    @Column(name = "city", nullable = false)
    val city: String,

    @field:NotBlank(message = "Postal code cannot be blank")
    @field:Pattern(
        regexp = "^[0-9]{5}$",
        message = "Finnish postal code must be exactly 5 digits"
    )
    @Column(name = "postal_code", nullable = false)
    val postalCode: String,
) {
    companion object {
        const val COUNTRY = "Finland"
        private val FINNISH_POSTAL_CODE_REGEX = Regex("^[0-9]{5}$")

        fun create(
            street: String,
            city: String,
            postalCode: String
        ): Address {
            require(street.isNotBlank()) { "Street address cannot be blank" }
            require(city.isNotBlank()) { "City cannot be blank" }
            require(postalCode.isNotBlank()) { "Postal code cannot be blank" }
            require(isValidFinnishPostalCode(postalCode)) { "Invalid Finnish postal code format" }
            return Address(street, city, postalCode)
        }

        private fun isValidFinnishPostalCode(code: String): Boolean {
            return FINNISH_POSTAL_CODE_REGEX.matches(code)
        }
    }

    fun getFullAddress(): String {
        return "$street, $city, $postalCode, $COUNTRY"
    }

    fun isInFinland(): Boolean {
        return true // Always true since this system only supports Finland
    }
}
