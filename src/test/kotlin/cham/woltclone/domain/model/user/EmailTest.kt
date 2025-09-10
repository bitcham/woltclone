package cham.woltclone.domain.model.user

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.kotest.assertions.throwables.shouldThrow

class EmailTest : BehaviorSpec({
    
    given("valid email addresses") {
        `when`("creating email with valid format") {
            val email = Email("test@example.com")
            
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
                    Email("")
                }
            }
        }
        
        `when`("creating email without @ symbol") {
            then("should throw exception") {
                shouldThrow<IllegalArgumentException> {
                    Email("invalid-email")
                }
            }
        }
        
        `when`("creating email without domain") {
            then("should throw exception") {
                shouldThrow<IllegalArgumentException> {
                    Email("test@")
                }
            }
        }
        
        `when`("creating email without local part") {
            then("should throw exception") {
                shouldThrow<IllegalArgumentException> {
                    Email("@example.com")
                }
            }
        }
        
        `when`("creating email with invalid domain") {
            then("should throw exception") {
                shouldThrow<IllegalArgumentException> {
                    Email("test@invalid")  // No TLD
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