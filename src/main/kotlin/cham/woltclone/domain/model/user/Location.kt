package cham.woltclone.domain.model.user

import jakarta.persistence.Column
import jakarta.persistence.Embeddable
import jakarta.validation.constraints.DecimalMax
import jakarta.validation.constraints.DecimalMin
import kotlin.math.*

@Embeddable
data class Location(
    @field:DecimalMin(value = "59.0", message = "Latitude must be within Finland (59.0 to 70.0)")
    @field:DecimalMax(value = "70.0", message = "Latitude must be within Finland (59.0 to 70.0)")
    @Column(name = "latitude", nullable = false)
    val latitude: Double,

    @field:DecimalMin(value = "19.0", message = "Longitude must be within Finland (19.0 to 32.0)")
    @field:DecimalMax(value = "32.0", message = "Longitude must be within Finland (19.0 to 32.0)")
    @Column(name = "longitude", nullable = false)
    val longitude: Double
) {
    companion object {
        const val MIN_LATITUDE = 59.0
        const val MAX_LATITUDE = 70.0
        const val MIN_LONGITUDE = 19.0
        const val MAX_LONGITUDE = 32.0
    }

    init {
        require(latitude in MIN_LATITUDE..MAX_LATITUDE) { 
            "Latitude must be within Finland boundaries ($MIN_LATITUDE to $MAX_LATITUDE)" 
        }
        require(longitude in MIN_LONGITUDE..MAX_LONGITUDE) { 
            "Longitude must be within Finland boundaries ($MIN_LONGITUDE to $MAX_LONGITUDE)" 
        }
    }

    /**
     * Calculate distance to another location using Haversine formula
     * @return distance in kilometers
     */
    fun distanceTo(other: Location): Double {
        val earthRadiusKm = 6371.0
        
        val dLat = Math.toRadians(other.latitude - latitude)
        val dLon = Math.toRadians(other.longitude - longitude)
        
        val lat1Rad = Math.toRadians(latitude)
        val lat2Rad = Math.toRadians(other.latitude)
        
        val a = sin(dLat / 2).pow(2) + sin(dLon / 2).pow(2) * cos(lat1Rad) * cos(lat2Rad)
        val c = 2 * asin(sqrt(a))
        
        return earthRadiusKm * c
    }

    /**
     * Check if location is within delivery radius of another location
     */
    fun isWithinDeliveryRadius(center: Location, radiusKm: Double): Boolean {
        return distanceTo(center) <= radiusKm
    }

    fun toCoordinateString(): String {
        return "$latitude,$longitude"
    }

    fun isInFinland(): Boolean {
        return true // Always true since this system only supports Finland
    }
}