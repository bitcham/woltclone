package cham.woltclone.domain.model.user

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.kotest.assertions.throwables.shouldThrow

class AddressTest : BehaviorSpec({
    
    given("valid Finnish address components") {
        val validStreet = "Mannerheimintie 12"
        val validCity = "Helsinki"
        val validPostalCode = "00100"
        
        `when`("creating a Finnish address") {
            val address = Address(
                street = validStreet,
                city = validCity,
                postalCode = validPostalCode
            )
            
            then("address should be created successfully") {
                address.street shouldBe validStreet
                address.city shouldBe validCity
                address.postalCode shouldBe validPostalCode
            }
            
            then("should always be in Finland") {
                address.isInFinland() shouldBe true
            }
        }
        
        `when`("getting full address") {
            val address = Address(
                street = validStreet,
                city = validCity,
                postalCode = validPostalCode
            )
            
            then("should return formatted full address with Finland") {
                address.getFullAddress() shouldBe "Mannerheimintie 12, Helsinki, 00100, Finland"
            }
        }
    }
    
    given("various valid Finnish postal codes") {
        val validPostalCodes = listOf("00100", "20100", "33100", "40100", "96100")
        
        validPostalCodes.forEach { postalCode ->
            `when`("creating address with postal code $postalCode") {
                val address = Address(
                    street = "Test Street 1",
                    city = "Test City",
                    postalCode = postalCode
                )
                
                then("should be created successfully") {
                    address.postalCode shouldBe postalCode
                }
            }
        }
    }
    
    given("invalid address components") {
        `when`("creating address with blank street") {
            then("should throw exception") {
                shouldThrow<IllegalArgumentException> {
                    Address(
                        street = "",
                        city = "Helsinki",
                        postalCode = "00100"
                    )
                }
            }
        }
        
        `when`("creating address with blank city") {
            then("should throw exception") {
                shouldThrow<IllegalArgumentException> {
                    Address(
                        street = "Mannerheimintie 12",
                        city = "",
                        postalCode = "00100"
                    )
                }
            }
        }
        
        `when`("creating address with blank postal code") {
            then("should throw exception") {
                shouldThrow<IllegalArgumentException> {
                    Address(
                        street = "Mannerheimintie 12",
                        city = "Helsinki",
                        postalCode = ""
                    )
                }
            }
        }
    }
    
    given("invalid Finnish postal codes") {
        val invalidPostalCodes = listOf(
            "1234",      // Too short
            "123456",    // Too long
            "1234a",     // Contains letter
            "12-34",     // Contains hyphen
            "12 345",    // Contains space
            "ABCDE"      // All letters
        )
        
        invalidPostalCodes.forEach { invalidCode ->
            `when`("creating address with invalid postal code '$invalidCode'") {
                then("should throw exception") {
                    shouldThrow<IllegalArgumentException> {
                        Address(
                            street = "Test Street 1",
                            city = "Helsinki",
                            postalCode = invalidCode
                        )
                    }
                }
            }
        }
    }
})