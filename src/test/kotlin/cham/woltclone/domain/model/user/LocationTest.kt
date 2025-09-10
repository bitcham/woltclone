package cham.woltclone.domain.model.user

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.doubles.shouldBeGreaterThan
import io.kotest.matchers.doubles.shouldBeLessThan
import io.kotest.assertions.throwables.shouldThrow

class LocationTest : BehaviorSpec({
    
    given("valid Finnish locations") {
        val helsinkiLocation = Location(60.1699, 24.9384) // Helsinki coordinates
        val tampereLocation = Location(61.4978, 23.7610) // Tampere coordinates
        
        `when`("creating locations within Finland") {
            then("should be created successfully") {
                helsinkiLocation.latitude shouldBe 60.1699
                helsinkiLocation.longitude shouldBe 24.9384
                helsinkiLocation.isInFinland() shouldBe true
            }
        }
        
        `when`("calculating distance between cities") {
            val distance = helsinkiLocation.distanceTo(tampereLocation)
            
            then("should return reasonable distance") {
                distance shouldBeGreaterThan 150.0 // Approximate distance Helsinki-Tampere
                distance shouldBeLessThan 200.0
            }
        }
        
        `when`("checking delivery radius") {
            then("should correctly identify if within radius") {
                helsinkiLocation.isWithinDeliveryRadius(tampereLocation, 200.0) shouldBe true
                helsinkiLocation.isWithinDeliveryRadius(tampereLocation, 100.0) shouldBe false
            }
        }
        
        `when`("converting to coordinate string") {
            then("should format correctly") {
                helsinkiLocation.toCoordinateString() shouldBe "60.1699,24.9384"
            }
        }
    }
    
    given("boundary locations") {
        `when`("creating location at Finland's boundaries") {
            val northernmost = Location(70.0, 25.0) // Northern boundary
            val southernmost = Location(59.0, 25.0) // Southern boundary
            val easternmost = Location(65.0, 32.0) // Eastern boundary
            val westernmost = Location(65.0, 19.0) // Western boundary
            
            then("should be created successfully") {
                northernmost.isInFinland() shouldBe true
                southernmost.isInFinland() shouldBe true
                easternmost.isInFinland() shouldBe true
                westernmost.isInFinland() shouldBe true
            }
        }
    }
    
    given("invalid locations outside Finland") {
        `when`("creating location with latitude too low") {
            then("should throw exception") {
                shouldThrow<IllegalArgumentException> {
                    Location(58.9, 25.0) // Just south of Finland
                }
            }
        }
        
        `when`("creating location with latitude too high") {
            then("should throw exception") {
                shouldThrow<IllegalArgumentException> {
                    Location(70.1, 25.0) // Just north of Finland
                }
            }
        }
        
        `when`("creating location with longitude too low") {
            then("should throw exception") {
                shouldThrow<IllegalArgumentException> {
                    Location(65.0, 18.9) // Just west of Finland
                }
            }
        }
        
        `when`("creating location with longitude too high") {
            then("should throw exception") {
                shouldThrow<IllegalArgumentException> {
                    Location(65.0, 32.1) // Just east of Finland
                }
            }
        }
    }
    
    given("locations outside world coordinates") {
        `when`("creating location with invalid coordinates") {
            then("should throw exception for extreme values") {
                shouldThrow<IllegalArgumentException> {
                    Location(-100.0, 25.0) // Invalid latitude
                }
                shouldThrow<IllegalArgumentException> {
                    Location(65.0, -200.0) // Invalid longitude
                }
            }
        }
    }
})