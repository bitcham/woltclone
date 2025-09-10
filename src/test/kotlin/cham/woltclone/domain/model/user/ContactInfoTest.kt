package cham.woltclone.domain.model.user

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.kotest.assertions.throwables.shouldThrow

class ContactInfoTest : BehaviorSpec({
    
    given("valid contact information") {
        val validPhone = "+1234567890"
        
        `when`("creating contact info") {
            val contactInfo = ContactInfo(
                phone = validPhone
            )
            
            then("contact info should be created successfully") {
                contactInfo.phone shouldBe validPhone
            }
        }
        
        `when`("getting formatted phone") {
            val contactInfoWithPlus = ContactInfo(
                phone = "+1234567890"
            )
            val contactInfoWithoutPlus = ContactInfo(
                phone = "1234567890"
            )
            
            then("should format phone numbers correctly") {
                contactInfoWithPlus.getFormattedPhone() shouldBe "+1234567890"
                contactInfoWithoutPlus.getFormattedPhone() shouldBe "+1234567890"
            }
        }
        
    }
    
    given("invalid contact information") {
        `when`("creating contact info with blank phone") {
            then("should throw exception") {
                shouldThrow<IllegalArgumentException> {
                    ContactInfo(
                        phone = ""
                    )
                }
            }
        }
        
        `when`("creating contact info with invalid phone format") {
            then("should throw exception") {
                shouldThrow<IllegalArgumentException> {
                    ContactInfo(
                        phone = "12"  // Too short (less than 3 digits)
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