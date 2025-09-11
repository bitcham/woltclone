package cham.woltclone.application.required

import cham.woltclone.domain.user.*
import cham.woltclone.domain.user.Location
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.extensions.spring.SpringExtension
import io.kotest.matchers.collections.shouldContain
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.collections.shouldNotContain
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import jakarta.persistence.EntityManager
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest

@DataJpaTest
class UserRepositoryTest : BehaviorSpec() {

    override fun extensions() = listOf(SpringExtension)
    
    @Autowired
    private lateinit var userRepository: UserRepository
    
    @Autowired
    private lateinit var entityManager: EntityManager

    private fun createTestUser() = User.create(
        email = Email.of("test@example.com"),
        password = "password123",
        address = Address.create(
            street = "Mannerheimintie 12",
            city = "Helsinki",
            postalCode = "00100"
        ),
        contactInfo = ContactInfo.create(
            firstName = "John",
            lastName = "Doe",
            phoneNumber = "+1234567890"
        ),
        location = Location.create(60.1699, 24.9384)
    )
    
    init {
        beforeEach {
            entityManager.clear()
        }
        
        given("saving a user to the repository") {
            `when`("saving a valid user") {
                then("should return the saved user with generated ID") {
                    val testUser = createTestUser()
                    val savedUser = userRepository.save(testUser)
                    entityManager.flush()
                    entityManager.clear()
                    
                    savedUser.id shouldNotBe 0L
                    savedUser.email.value shouldBe "test@example.com"
                    savedUser.contactInfo.firstName shouldBe "John"
                    savedUser.contactInfo.lastName shouldBe "Doe"
                }
            }
        }
        
        given("finding a user by ID") {
            `when`("the user exists") {
                then("should return the correct user") {
                    val testUser = createTestUser()
                    val savedUser = userRepository.save(testUser)
                    entityManager.flush()
                    entityManager.clear()
                    
                    val foundUser = userRepository.findById(savedUser.id!!)
                    
                    foundUser shouldNotBe null
                    foundUser?.email?.value shouldBe "test@example.com"
                    foundUser?.contactInfo?.firstName shouldBe "John"
                }
            }
            
            `when`("the user does not exist") {
                then("should return null") {
                    val notFoundUser = userRepository.findById(99999L)
                    notFoundUser shouldBe null
                }
            }
        }
        
        given("finding a user by email") {
            `when`("the email exists") {
                then("should return the correct user") {
                    val testUser = createTestUser()
                    userRepository.save(testUser)
                    entityManager.flush()
                    entityManager.clear()
                    
                    val foundUser = userRepository.findByEmailValue("test@example.com")
                    
                    foundUser shouldNotBe null
                    foundUser?.email?.value shouldBe "test@example.com"
                    foundUser?.contactInfo?.firstName shouldBe "John"
                }
            }
            
            `when`("the email does not exist") {
                then("should return null") {
                    val notFoundUser = userRepository.findByEmailValue("nonexistent@example.com")
                    notFoundUser shouldBe null
                }
            }
        }
        
        given("checking if an email exists") {
            `when`("the email exists in the database") {
                then("should return true") {
                    val testUser = createTestUser()
                    userRepository.save(testUser)
                    entityManager.flush()
                    
                    val exists = userRepository.existsByEmailValue("test@example.com")
                    exists shouldBe true
                }
            }
            
            `when`("the email does not exist in the database") {
                then("should return false") {
                    val notExists = userRepository.existsByEmailValue("nonexistent@example.com")
                    notExists shouldBe false
                }
            }
        }
        
        given("finding all users") {
            `when`("multiple users are saved") {
                then("should return all saved users") {
                    val testUser = createTestUser()
                    val user1 = userRepository.save(testUser)
                    val user2 = userRepository.save(
                        User.create(
                            email = Email.of("user2@example.com"),
                            password = "password123",
                            address = Address.create(
                                street = "Aleksanterinkatu 15",
                                city = "Helsinki",
                                postalCode = "00100"
                            ),
                            contactInfo = ContactInfo.create("Jane", "Smith", "+9876543210"),
                            location = Location.create(60.1699, 24.9384)
                        )
                    )
                    entityManager.flush()
                    entityManager.clear()
                    
                    val allUsers = userRepository.findAll()
                    
                    allUsers shouldHaveSize 2
                    allUsers shouldContain user1
                    allUsers shouldContain user2
                }
            }
        }
        
        given("finding active users") {
            `when`("there are both active and inactive users") {
                then("should return only active users") {
                    val testUser = createTestUser()
                    val activeUser = userRepository.save(testUser)
                    val inactiveUser = userRepository.save(
                        User.create(
                            email = Email.of("inactive@example.com"),
                            password = "password123",
                            address = Address.create(
                                street = "Unioninkatu 5",
                                city = "Helsinki",
                                postalCode = "00130"
                            ),
                            contactInfo = ContactInfo.create("Bob", "Wilson", "+5555555555"),
                            location = Location.create(60.1699, 24.9384)
                        ).deactivate()
                    )
                    entityManager.flush()
                    entityManager.clear()
                    
                    val activeUsers = userRepository.findActiveUsers()
                    
                    activeUsers shouldHaveSize 1
                    activeUsers shouldContain activeUser
                    activeUsers shouldNotContain inactiveUser
                }
            }
        }
        
        given("deleting a user") {
            `when`("deleting by ID") {
                then("should remove the user from database") {
                    val testUser = createTestUser()
                    val savedUser = userRepository.save(testUser)
                    entityManager.flush()
                    
                    userRepository.deleteById(savedUser.id!!)
                    entityManager.flush()
                    entityManager.clear()
                    
                    val deletedUser = userRepository.findById(savedUser.id!!)
                    deletedUser shouldBe null
                }
            }
        }
        
        given("updating a user") {
            `when`("updating contact information") {
                then("should persist the changes") {
                    val testUser = createTestUser()
                    val savedUser = userRepository.save(testUser)
                    entityManager.flush()
                    entityManager.clear()
                    
                    val updatedUser = savedUser.updateContactInfo(
                        ContactInfo.create("Updated", "Name", "+1111111111")
                    )
                    
                    userRepository.save(updatedUser)
                    entityManager.flush()
                    entityManager.clear()
                    
                    val foundUser = userRepository.findById(savedUser.id!!)
                    foundUser?.contactInfo?.firstName shouldBe "Updated"
                    foundUser?.contactInfo?.lastName shouldBe "Name"
                    foundUser?.contactInfo?.phoneNumber shouldBe "+1111111111"
                    foundUser?.updatedAt?.isAfter(foundUser.createdAt) shouldBe true
                }
            }
        }
        
        given("concurrent operations") {
            `when`("handling multiple simultaneous reads") {
                then("should process all operations correctly") {
                    val testUser = createTestUser()
                    val user1 = userRepository.save(testUser)
                    val user2 = userRepository.save(
                        User.create(
                            email = Email.of("concurrent@example.com"),
                            password = "password123",
                            address = Address.create(
                                street = "Esplanadi 1",
                                city = "Helsinki",
                                postalCode = "00101"
                            ),
                            contactInfo = ContactInfo.create("Concurrent", "User", "+9999999999"),
                            location = Location.create(60.1699, 24.9384)
                        )
                    )
                    entityManager.flush()
                    
                    val foundUser1 = userRepository.findById(user1.id!!)
                    val foundUser2 = userRepository.findById(user2.id!!)
                    val allUsers = userRepository.findAll()
                    
                    foundUser1 shouldNotBe null
                    foundUser2 shouldNotBe null
                    allUsers shouldHaveSize 2
                }
            }
        }
    }
}