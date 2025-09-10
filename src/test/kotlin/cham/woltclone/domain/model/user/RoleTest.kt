package cham.woltclone.domain.model.user

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.collections.shouldContain
import io.kotest.matchers.collections.shouldNotContain

class RoleTest : BehaviorSpec({
    
    given("Role permissions") {
        `when`("checking CUSTOMER permissions") {
            val customerRole = Role.CUSTOMER
            
            then("should have customer-specific permissions") {
                customerRole.hasPermission(Permission.PLACE_ORDER) shouldBe true
                customerRole.hasPermission(Permission.VIEW_ORDER_HISTORY) shouldBe true
                customerRole.hasPermission(Permission.CANCEL_ORDER) shouldBe true
                customerRole.hasPermission(Permission.RATE_MERCHANT) shouldBe true
            }
            
            then("should not have merchant permissions") {
                customerRole.hasPermission(Permission.MANAGE_MENU) shouldBe false
                customerRole.hasPermission(Permission.ACCEPT_ORDER) shouldBe false
            }
            
            then("should not have courier permissions") {
                customerRole.hasPermission(Permission.VIEW_DELIVERY_REQUESTS) shouldBe false
                customerRole.hasPermission(Permission.ACCEPT_DELIVERY) shouldBe false
            }
        }
        
        `when`("checking MERCHANT permissions") {
            val merchantRole = Role.MERCHANT
            
            then("should have merchant-specific permissions") {
                merchantRole.hasPermission(Permission.MANAGE_MENU) shouldBe true
                merchantRole.hasPermission(Permission.VIEW_ORDERS) shouldBe true
                merchantRole.hasPermission(Permission.ACCEPT_ORDER) shouldBe true
                merchantRole.hasPermission(Permission.REJECT_ORDER) shouldBe true
            }
            
            then("should also have customer permissions") {
                merchantRole.hasPermission(Permission.PLACE_ORDER) shouldBe true
                merchantRole.hasPermission(Permission.VIEW_ORDER_HISTORY) shouldBe true
            }
            
            then("should not have courier permissions") {
                merchantRole.hasPermission(Permission.VIEW_DELIVERY_REQUESTS) shouldBe false
                merchantRole.hasPermission(Permission.ACCEPT_DELIVERY) shouldBe false
            }
        }
        
        `when`("checking COURIER permissions") {
            val courierRole = Role.COURIER
            
            then("should have courier-specific permissions") {
                courierRole.hasPermission(Permission.VIEW_DELIVERY_REQUESTS) shouldBe true
                courierRole.hasPermission(Permission.ACCEPT_DELIVERY) shouldBe true
                courierRole.hasPermission(Permission.UPDATE_DELIVERY_STATUS) shouldBe true
            }
            
            then("should also have customer permissions") {
                courierRole.hasPermission(Permission.PLACE_ORDER) shouldBe true
                courierRole.hasPermission(Permission.VIEW_ORDER_HISTORY) shouldBe true
            }
            
            then("should not have merchant permissions") {
                courierRole.hasPermission(Permission.MANAGE_MENU) shouldBe false
                courierRole.hasPermission(Permission.ACCEPT_ORDER) shouldBe false
            }
        }
        
        `when`("checking ADMIN permissions") {
            val adminRole = Role.ADMIN
            
            then("should have all permissions") {
                Permission.values().forEach { permission ->
                    adminRole.hasPermission(permission) shouldBe true
                }
            }
        }
    }
    
    given("Role capabilities") {
        `when`("checking role capabilities") {
            then("should have correct order placement capabilities") {
                Role.CUSTOMER.canPlaceOrder() shouldBe true
                Role.MERCHANT.canPlaceOrder() shouldBe true
                Role.COURIER.canPlaceOrder() shouldBe true
                Role.ADMIN.canPlaceOrder() shouldBe true
            }
            
            then("should have correct merchant management capabilities") {
                Role.CUSTOMER.canManageMerchant() shouldBe false
                Role.MERCHANT.canManageMerchant() shouldBe true
                Role.COURIER.canManageMerchant() shouldBe false
                Role.ADMIN.canManageMerchant() shouldBe true
            }
            
            then("should have correct delivery capabilities") {
                Role.CUSTOMER.canDeliverOrder() shouldBe false
                Role.MERCHANT.canDeliverOrder() shouldBe false
                Role.COURIER.canDeliverOrder() shouldBe true
                Role.ADMIN.canDeliverOrder() shouldBe true
            }
        }
    }
    
    given("Role display and description") {
        `when`("getting display names") {
            then("should return properly formatted names") {
                Role.CUSTOMER.getDisplayName() shouldBe "Customer"
                Role.MERCHANT.getDisplayName() shouldBe "Merchant"
                Role.COURIER.getDisplayName() shouldBe "Courier"
                Role.ADMIN.getDisplayName() shouldBe "Admin"
            }
        }
        
        `when`("getting descriptions") {
            then("should return meaningful descriptions") {
                Role.CUSTOMER.getDescription() shouldBe "Can place orders and rate merchants"
                Role.MERCHANT.getDescription() shouldBe "Can manage restaurant menu and orders"
                Role.COURIER.getDescription() shouldBe "Can accept and deliver orders"
                Role.ADMIN.getDescription() shouldBe "Has full system access and management capabilities"
            }
        }
    }
})