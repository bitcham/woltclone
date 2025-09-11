package cham.woltclone.application.provided.user

import cham.woltclone.application.required.UserRepository
import cham.woltclone.domain.model.user.*
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify

class UserRegisterTest : BehaviorSpec({
    
    val userRepository = mockk<UserRepository>()
    val userRegister = UserRegister(userRepository)
    
    Given("a valid registration command") {
        val command = UserRegisterCommand(
            email = "test@example.com",
            password = "password123",
            confirmPassword = "password123",
            firstName = "John",
            lastName = "Doe",
            phoneNumber = "+1234567890",
            street = "Mannerheimintie 12",
            city = "Helsinki",
            postalCode = "00100",
            latitude = 60.1699,
            longitude = 24.9384
        )
        
        When("the email does not exist") {
            every { userRepository.existsByEmailValue(any()) } returns false
            every { userRepository.save(any()) } answers { firstArg() }
            
            Then("should successfully register the user") {
                val result = userRegister.execute(command)
                
                result shouldNotBe null
                result.email.value shouldBe command.email
                result.contactInfo.firstName shouldBe command.firstName
                result.contactInfo.lastName shouldBe command.lastName
                result.address.street shouldBe command.street
                result.location shouldNotBe null
                result.location?.latitude shouldBe command.latitude
                result.location?.longitude shouldBe command.longitude
                
                verify { userRepository.existsByEmailValue(command.email) }
                verify { userRepository.save(any()) }
            }
        }
        
        When("the email already exists") {
            every { userRepository.existsByEmailValue(any()) } returns true
            
            Then("should throw UserAlreadyExistsException") {
                val exception = shouldThrow<UserAlreadyExistsException> {
                    userRegister.execute(command)
                }
                exception.message shouldBe "User with email ${command.email} already exists"
            }
        }
    }
    
    Given("a registration command without location") {
        val command = UserRegisterCommand(
            email = "test@example.com",
            password = "password123",
            confirmPassword = "password123",
            firstName = "John",
            lastName = "Doe",
            phoneNumber = "+1234567890",
            street = "Aleksanterinkatu 15",
            city = "Helsinki",
            postalCode = "00100"
        )
        
        When("registering the user") {
            every { userRepository.existsByEmailValue(any()) } returns false
            every { userRepository.save(any()) } answers { firstArg() }
            
            Then("should create user without location") {
                val result = userRegister.execute(command)
                
                result.location shouldBe null
            }
        }
    }
    
    Given("invalid registration commands") {
        When("passwords do not match") {
            val command = UserRegisterCommand(
                email = "test@example.com",
                password = "password123",
                confirmPassword = "differentpassword",
                firstName = "John",
                lastName = "Doe",
                phoneNumber = "+1234567890",
                street = "Unioninkatu 5",
                city = "Helsinki",
                postalCode = "00130"
            )
            
            Then("should throw IllegalArgumentException") {
                val exception = shouldThrow<IllegalArgumentException> {
                    userRegister.execute(command)
                }
                exception.message shouldBe "Password and confirm password must match"
            }
        }
        
        When("first name is blank") {
            val command = UserRegisterCommand(
                email = "test@example.com",
                password = "password123",
                confirmPassword = "password123",
                firstName = "",
                lastName = "Doe",
                phoneNumber = "+1234567890",
                street = "Esplanadi 1",
                city = "Helsinki",
                postalCode = "00101"
            )
            
            Then("should throw IllegalArgumentException") {
                val exception = shouldThrow<IllegalArgumentException> {
                    userRegister.execute(command)
                }
                exception.message shouldBe "First name cannot be blank"
            }
        }
        
        When("email is invalid") {
            val command = UserRegisterCommand(
                email = "invalid-email",
                password = "password123",
                confirmPassword = "password123",
                firstName = "John",
                lastName = "Doe",
                phoneNumber = "+1234567890",
                street = "Kaivokatu 10",
                city = "Helsinki",
                postalCode = "00100"
            )
            
            every { userRepository.existsByEmailValue(any()) } returns false
            
            Then("should throw IllegalArgumentException from Email validation") {
                shouldThrow<IllegalArgumentException> {
                    userRegister.execute(command)
                }
            }
        }
    }
})