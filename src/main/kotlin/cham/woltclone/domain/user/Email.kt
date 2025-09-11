package cham.woltclone.domain.user

import jakarta.persistence.Column
import jakarta.persistence.Embeddable
import jakarta.validation.constraints.Size

@Embeddable
data class Email private constructor(
    @field:Size(max = 255, message = "Email cannot exceed 255 characters")
    @Column(name = "email", nullable = false, unique = true)
    val value: String
) {
    companion object {
        private val EMAIL_PATTERN = Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")

        fun of(email: String): Email {
            require(email.isNotBlank()) { "Email cannot be blank" }
            require(isValidEmail(email)) { "Email must be a valid email address" }
            return Email(email)
        }

        fun isValidEmail(email: String): Boolean {
            return EMAIL_PATTERN.matches(email)
        }
    }

    override fun toString(): String {
        return value
    }
}