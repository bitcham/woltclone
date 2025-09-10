package cham.woltclone.domain.model.user

import jakarta.persistence.Column
import jakarta.persistence.Embedded
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import java.time.LocalDateTime

@Entity
@Table(name = "users")
data class User(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Embedded
    val email: Email,

    @Column(nullable = false)
    private val passwordHash: String,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    val role: Role = Role.CUSTOMER,

    @Embedded
    val address: Address,

    @Embedded
    val contactInfo: ContactInfo,

    @Embedded
    val location: Location? = null,

    @Column(nullable = false)
    val isActive: Boolean = true,

    @Column(nullable = false)
    val createdAt: LocalDateTime = LocalDateTime.now(),

    @Column(nullable = false)
    val updatedAt: LocalDateTime = LocalDateTime.now()
) {
    companion object {
        private val passwordEncoder = BCryptPasswordEncoder()
        
        fun create(
            email: Email,
            password: String,
            address: Address,
            contactInfo: ContactInfo,
            role: Role = Role.CUSTOMER,
            location: Location? = null
        ): User {
            require(password.isNotBlank()) { "Password cannot be blank" }
            require(password.length >= 8) { "Password must be at least 8 characters" }
            
            return User(
                email = email,
                passwordHash = passwordEncoder.encode(password),
                role = role,
                address = address,
                contactInfo = contactInfo,
                location = location
            )
        }
    }

    fun changePassword(newPassword: String): User {
        require(newPassword.isNotBlank()) { "Password cannot be blank" }
        require(newPassword.length >= 8) { "Password must be at least 8 characters" }
        return copy(
            passwordHash = passwordEncoder.encode(newPassword),
            updatedAt = LocalDateTime.now()
        )
    }

    fun verifyPassword(password: String): Boolean {
        return passwordEncoder.matches(password, passwordHash)
    }

    fun updateRole(newRole: Role): User {
        return copy(role = newRole, updatedAt = LocalDateTime.now())
    }

    fun updateAddress(newAddress: Address): User {
        return copy(address = newAddress, updatedAt = LocalDateTime.now())
    }

    fun updateContactInfo(newContactInfo: ContactInfo): User {
        return copy(contactInfo = newContactInfo, updatedAt = LocalDateTime.now())
    }

    fun updateLocation(newLocation: Location?): User {
        return copy(location = newLocation, updatedAt = LocalDateTime.now())
    }

    fun deactivate(): User {
        return copy(isActive = false, updatedAt = LocalDateTime.now())
    }

    fun activate(): User {
        return copy(isActive = true, updatedAt = LocalDateTime.now())
    }

    fun hasPermission(permission: Permission): Boolean {
        return role.hasPermission(permission)
    }

    fun canPlaceOrder(): Boolean = isActive && role.canPlaceOrder()
    fun canManageMerchant(): Boolean = isActive && role.canManageMerchant()
    fun canDeliverOrder(): Boolean = isActive && role.canDeliverOrder()
    fun isAdmin(): Boolean = role == Role.ADMIN

    fun hasLocation(): Boolean = location != null
    fun getDistanceTo(otherLocation: Location): Double? {
        return location?.distanceTo(otherLocation)
    }
    fun isWithinDeliveryRadius(center: Location, radiusKm: Double): Boolean {
        return location?.isWithinDeliveryRadius(center, radiusKm) ?: false
    }
}