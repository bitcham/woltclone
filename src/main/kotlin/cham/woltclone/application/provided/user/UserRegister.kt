package cham.woltclone.application.provided.user

import cham.woltclone.application.required.UserRepository
import cham.woltclone.domain.user.Address
import cham.woltclone.domain.user.ContactInfo
import cham.woltclone.domain.user.Email
import cham.woltclone.domain.user.Location
import cham.woltclone.domain.user.User

class UserRegister(
    private val userRepository: UserRepository
) {
    
    fun execute(command: UserRegisterCommand): User {
        validateCommand(command)
        
        val email = Email.of(command.email)
        
        if (userRepository.existsByEmailValue(command.email)) {
            throw UserAlreadyExistsException("User with email ${command.email} already exists")
        }
        
        val address = Address.create(
            street = command.street,
            city = command.city,
            postalCode = command.postalCode,
        )
        
        val contactInfo = ContactInfo.create(
            firstName = command.firstName,
            lastName = command.lastName,
            phoneNumber = command.phoneNumber
        )
        
        val location = if (command.latitude != null && command.longitude != null) {
            Location.create(command.latitude, command.longitude)
        } else null
        
        val user = User.create(
            email = email,
            password = command.password,
            address = address,
            contactInfo = contactInfo,
            location = location
        )
        
        return userRepository.save(user)
    }
    
    private fun validateCommand(command: UserRegisterCommand) {
        require(command.password == command.confirmPassword) { 
            "Password and confirm password must match" 
        }
        require(command.firstName.isNotBlank()) { "First name cannot be blank" }
        require(command.lastName.isNotBlank()) { "Last name cannot be blank" }
        require(command.phoneNumber.isNotBlank()) { "Phone number cannot be blank" }
        require(command.street.isNotBlank()) { "Street cannot be blank" }
        require(command.city.isNotBlank()) { "City cannot be blank" }
        require(command.postalCode.isNotBlank()) { "Postal code cannot be blank" }
    }
}

class UserAlreadyExistsException(message: String) : RuntimeException(message)