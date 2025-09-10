package cham.woltclone.domain.model.user

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.kotest.matchers.doubles.shouldBeGreaterThan
import io.kotest.matchers.doubles.shouldBeLessThan
import io.kotest.assertions.throwables.shouldThrow
import java.time.LocalDateTime

class UserTest : BehaviorSpec({
    
    given("a valid user") {
        val validAddress = Address(
            street = "Mannerheimintie 12",
            city = "Helsinki",
            postalCode = "00100"
        )
        val validContactInfo = ContactInfo(
            phone = "+1234567890"
        )
        val validLocation = Location(60.1699, 24.9384) // Helsinki coordinates
        
        `when`("creating a user") {
            val user = User.create(
                email = Email("user@example.com"),
                password = "password123",
                address = validAddress,
                contactInfo = validContactInfo
            )
            
            then("user should have correct default values") {
                user.role shouldBe Role.CUSTOMER
                user.isActive shouldBe true
                user.createdAt shouldNotBe null
            }
        }
        
        `when`("creating user with invalid password") {
            then("should throw exception for blank password") {
                shouldThrow<IllegalArgumentException> {
                    User.create(
                        email = Email("user@example.com"),
                        password = "",
                        address = validAddress,
                        contactInfo = validContactInfo
                    )
                }
            }
            
            then("should throw exception for short password") {
                shouldThrow<IllegalArgumentException> {
                    User.create(
                        email = Email("user@example.com"),
                        password = "123",
                        address = validAddress,
                        contactInfo = validContactInfo
                    )
                }
            }
        }
        
        `when`("changing password") {
            val user = User.create(
                email = Email("user@example.com"),
                password = "password123",
                address = validAddress,
                contactInfo = validContactInfo
            )
            val newPassword = "newPassword123"
            val oldUpdatedAt = user.updatedAt
            
            val updatedUser = user.changePassword(newPassword)
            
            then("password should be updated and verified") {
                updatedUser.verifyPassword(newPassword) shouldBe true
                updatedUser.verifyPassword("wrongPassword") shouldBe false
                updatedUser.updatedAt shouldNotBe oldUpdatedAt
            }
        }
        
        `when`("changing password with invalid input") {
            val user = User.create(
                email = Email("user@example.com"),
                password = "password123",
                address = validAddress,
                contactInfo = validContactInfo
            )
            
            then("should throw exception for blank password") {
                shouldThrow<IllegalArgumentException> {
                    user.changePassword("")
                }
            }
            
            then("should throw exception for short password") {
                shouldThrow<IllegalArgumentException> {
                    user.changePassword("123")
                }
            }
        }
        
        `when`("updating role") {
            val user = User.create(
                email = Email("user@example.com"),
                password = "password123",
                address = validAddress,
                contactInfo = validContactInfo
            )
            
            val updatedUser = user.updateRole(Role.MERCHANT)
            
            then("role should be updated") {
                updatedUser.role shouldBe Role.MERCHANT
                updatedUser.canManageMerchant() shouldBe true
            }
        }
        
        `when`("checking permissions") {
            val customerUser = User.create(
                email = Email("customer@example.com"),
                password = "password123",
                address = validAddress,
                contactInfo = validContactInfo,
                role = Role.CUSTOMER
            )
            
            then("customer should have correct permissions") {
                customerUser.hasPermission(Permission.PLACE_ORDER) shouldBe true
                customerUser.hasPermission(Permission.MANAGE_MENU) shouldBe false
                customerUser.canPlaceOrder() shouldBe true
                customerUser.canManageMerchant() shouldBe false
            }
        }
        
        `when`("deactivating user") {
            val user = User.create(
                email = Email("user@example.com"),
                password = "password123",
                address = validAddress,
                contactInfo = validContactInfo
            )
            
            val deactivatedUser = user.deactivate()
            
            then("user should be inactive") {
                deactivatedUser.isActive shouldBe false
            }
        }
        
        `when`("activating user") {
            val user = User.create(
                email = Email("user@example.com"),
                password = "password123",
                address = validAddress,
                contactInfo = validContactInfo
            )
            
            val deactivatedUser = user.deactivate()
            val activatedUser = deactivatedUser.activate()
            
            then("user should be active again") {
                activatedUser.isActive shouldBe true
            }
        }
        
        `when`("creating user with location") {
            val user = User.create(
                email = Email("user@example.com"),
                password = "password123",
                address = validAddress,
                contactInfo = validContactInfo,
                location = validLocation
            )
            
            then("user should have location") {
                user.hasLocation() shouldBe true
                user.location shouldBe validLocation
            }
        }
        
        `when`("creating user without location") {
            val user = User.create(
                email = Email("user@example.com"),
                password = "password123",
                address = validAddress,
                contactInfo = validContactInfo
            )
            
            then("user should not have location") {
                user.hasLocation() shouldBe false
                user.location shouldBe null
            }
        }
        
        `when`("updating user location") {
            val user = User.create(
                email = Email("user@example.com"),
                password = "password123",
                address = validAddress,
                contactInfo = validContactInfo
            )
            
            val updatedUser = user.updateLocation(validLocation)
            
            then("location should be updated") {
                updatedUser.location shouldBe validLocation
                updatedUser.hasLocation() shouldBe true
            }
        }
        
        `when`("calculating distance to another location") {
            val userWithLocation = User.create(
                email = Email("user@example.com"),
                password = "password123",
                address = validAddress,
                contactInfo = validContactInfo,
                location = validLocation
            )
            
            val tampereLocation = Location(61.4978, 23.7610) // Tampere coordinates
            val distance = userWithLocation.getDistanceTo(tampereLocation)
            
            then("should return distance") {
                distance shouldNotBe null
                distance!! shouldBeGreaterThan 150.0
                distance shouldBeLessThan 200.0
            }
        }
        
        `when`("checking delivery radius") {
            val userWithLocation = User.create(
                email = Email("user@example.com"),
                password = "password123",
                address = validAddress,
                contactInfo = validContactInfo,
                location = validLocation
            )
            
            val nearbyLocation = Location(60.2, 24.9) // Close to Helsinki
            
            then("should correctly identify if within delivery radius") {
                userWithLocation.isWithinDeliveryRadius(nearbyLocation, 10.0) shouldBe true
                userWithLocation.isWithinDeliveryRadius(nearbyLocation, 1.0) shouldBe false
            }
        }
        
        `when`("user without location checks delivery radius") {
            val userWithoutLocation = User.create(
                email = Email("user@example.com"),
                password = "password123",
                address = validAddress,
                contactInfo = validContactInfo
            )
            
            val someLocation = Location(60.2, 24.9)
            
            then("should return false") {
                userWithoutLocation.isWithinDeliveryRadius(someLocation, 10.0) shouldBe false
                userWithoutLocation.getDistanceTo(someLocation) shouldBe null
            }
        }
    }
})