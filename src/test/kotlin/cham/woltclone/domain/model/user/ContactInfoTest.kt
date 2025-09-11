package cham.woltclone.domain.model.user

import cham.woltclone.domain.user.ContactInfo
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.kotest.assertions.throwables.shouldThrow

class ContactInfoTest : BehaviorSpec({
    
    given("valid contact information") {
        val validFirstName = "John"
        val validLastName = "Doe"
        val validPhone = "+1234567890"
        
        `when`("creating contact info") {
            val contactInfo = ContactInfo.create(
                firstName = validFirstName,
                lastName = validLastName,
                phoneNumber = validPhone
            )
            
            then("contact info should be created successfully") {
                contactInfo.firstName shouldBe validFirstName
                contactInfo.lastName shouldBe validLastName
                contactInfo.phoneNumber shouldBe validPhone
            }
            
            then("should provide full name") {
                contactInfo.getFullName() shouldBe "John Doe"
            }
        }
        
        `when`("getting formatted phone") {
            val contactInfoWithPlus = ContactInfo.create(
                firstName = "John",
                lastName = "Doe", 
                phoneNumber = "+1234567890"
            )
            val contactInfoWithoutPlus = ContactInfo.create(
                firstName = "Jane",
                lastName = "Smith",
                phoneNumber = "1234567890"
            )
            
            then("should format phone numbers correctly") {
                contactInfoWithPlus.getFormattedPhone() shouldBe "+1234567890"
                contactInfoWithoutPlus.getFormattedPhone() shouldBe "+1234567890"
            }
        }
        
    }
    
    given("invalid contact information") {
        `when`("creating contact info with blank first name") {
            then("should throw exception") {
                shouldThrow<IllegalArgumentException> {
                    ContactInfo.create(
                        firstName = "",
                        lastName = "Doe",
                        phoneNumber = "+1234567890"
                    )
                }
            }
        }
        
        `when`("creating contact info with blank last name") {
            then("should throw exception") {
                shouldThrow<IllegalArgumentException> {
                    ContactInfo.create(
                        firstName = "John",
                        lastName = "",
                        phoneNumber = "+1234567890"
                    )
                }
            }
        }
        
        `when`("creating contact info with blank phone") {
            then("should throw exception") {
                shouldThrow<IllegalArgumentException> {
                    ContactInfo.create(
                        firstName = "John",
                        lastName = "Doe",
                        phoneNumber = ""
                    )
                }
            }
        }
        
        `when`("creating contact info with invalid phone format") {
            then("should throw exception") {
                shouldThrow<IllegalArgumentException> {
                    ContactInfo.create(
                        firstName = "John",
                        lastName = "Doe",
                        phoneNumber = "12"  // Too short (less than 3 digits)
                    )
                }
            }
        }
    }
    
    given("validation helper methods") {
        `when`("validating phone numbers") {
            then("should correctly validate phone formats") {
                ContactInfo.isValidPhoneNumber("+1234567890") shouldBe true
                ContactInfo.isValidPhoneNumber("1234567890") shouldBe true
                ContactInfo.isValidPhoneNumber("+12345678901234") shouldBe true
                ContactInfo.isValidPhoneNumber("123") shouldBe true  // 3 digits is valid by regex
                ContactInfo.isValidPhoneNumber("12") shouldBe false  // Too short
                ContactInfo.isValidPhoneNumber("+0123456789") shouldBe false  // Starts with 0
                ContactInfo.isValidPhoneNumber("") shouldBe false  // Empty
                ContactInfo.isValidPhoneNumber("abc") shouldBe false  // Non-numeric
            }
        }
        
    }
})