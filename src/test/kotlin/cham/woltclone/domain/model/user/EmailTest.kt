package cham.woltclone.domain.model.user

import cham.woltclone.domain.user.Email
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.kotest.assertions.throwables.shouldThrow

class EmailTest : BehaviorSpec({
    
    given("valid email addresses") {
        `when`("creating email with valid format") {
            val email = Email.of("test@example.com")
            
            then("email should be created successfully") {
                email.value shouldBe "test@example.com"
                email.toString() shouldBe "test@example.com"
            }
        }
    }
    
    given("invalid email addresses") {
        `when`("creating email with blank value") {
            then("should throw exception") {
                shouldThrow<IllegalArgumentException> {
                    Email.of("")
                }
            }
        }
        
        `when`("creating email without @ symbol") {
            then("should throw exception") {
                shouldThrow<IllegalArgumentException> {
                    Email.of("invalid-email")
                }
            }
        }
        
        `when`("creating email without domain") {
            then("should throw exception") {
                shouldThrow<IllegalArgumentException> {
                    Email.of("test@")
                }
            }
        }
        
        `when`("creating email without local part") {
            then("should throw exception") {
                shouldThrow<IllegalArgumentException> {
                    Email.of("@example.com")
                }
            }
        }
        
        `when`("creating email with invalid domain") {
            then("should throw exception") {
                shouldThrow<IllegalArgumentException> {
                    Email.of("test@invalid")  // No TLD
                }
            }
        }
    }
    
    given("email validation helper") {
        `when`("validating various email formats") {
            then("should correctly validate email formats") {
                Email.isValidEmail("test@example.com") shouldBe true
                Email.isValidEmail("user.name+tag@example.co.uk") shouldBe true
                Email.isValidEmail("user123@test-domain.org") shouldBe true
                Email.isValidEmail("invalid-email") shouldBe false
                Email.isValidEmail("@example.com") shouldBe false
                Email.isValidEmail("test@") shouldBe false
                Email.isValidEmail("test@invalid") shouldBe false
                Email.isValidEmail("") shouldBe false
            }
        }
    }
})