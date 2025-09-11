package cham.woltclone.domain.user

enum class Role {
    CUSTOMER {
        override fun getPermissions(): Set<Permission> {
            return setOf(
                Permission.PLACE_ORDER,
                Permission.VIEW_ORDER_HISTORY,
                Permission.CANCEL_ORDER,
                Permission.RATE_MERCHANT
            )
        }
        
        override fun canPlaceOrder(): Boolean = true
        override fun canManageMerchant(): Boolean = false
        override fun canDeliverOrder(): Boolean = false
    },
    
    MERCHANT {
        override fun getPermissions(): Set<Permission> {
            return setOf(
                Permission.MANAGE_MENU,
                Permission.VIEW_ORDERS,
                Permission.ACCEPT_ORDER,
                Permission.REJECT_ORDER,
                Permission.UPDATE_ORDER_STATUS,
                Permission.VIEW_ANALYTICS,
                Permission.PLACE_ORDER,  // Merchants can also place orders
                Permission.VIEW_ORDER_HISTORY,
                Permission.CANCEL_ORDER
            )
        }
        
        override fun canPlaceOrder(): Boolean = true
        override fun canManageMerchant(): Boolean = true
        override fun canDeliverOrder(): Boolean = false
    },
    
    COURIER {
        override fun getPermissions(): Set<Permission> {
            return setOf(
                Permission.VIEW_DELIVERY_REQUESTS,
                Permission.ACCEPT_DELIVERY,
                Permission.UPDATE_DELIVERY_STATUS,
                Permission.VIEW_DELIVERY_HISTORY,
                Permission.PLACE_ORDER,  // Couriers can also place orders
                Permission.VIEW_ORDER_HISTORY,
                Permission.CANCEL_ORDER
            )
        }
        
        override fun canPlaceOrder(): Boolean = true
        override fun canManageMerchant(): Boolean = false
        override fun canDeliverOrder(): Boolean = true
    },
    
    ADMIN {
        override fun getPermissions(): Set<Permission> {
            return Permission.entries.toSet()  // Admin has all permissions
        }
        
        override fun canPlaceOrder(): Boolean = true
        override fun canManageMerchant(): Boolean = true
        override fun canDeliverOrder(): Boolean = true
    };
    
    abstract fun getPermissions(): Set<Permission>
    abstract fun canPlaceOrder(): Boolean
    abstract fun canManageMerchant(): Boolean
    abstract fun canDeliverOrder(): Boolean
    
    fun hasPermission(permission: Permission): Boolean {
        return getPermissions().contains(permission)
    }
    
    fun getDisplayName(): String {
        return this.name.lowercase().replaceFirstChar { it.uppercase() }
    }
    
    fun getDescription(): String {
        return when (this) {
            CUSTOMER -> "Can place orders and rate merchants"
            MERCHANT -> "Can manage restaurant menu and orders"
            COURIER -> "Can accept and deliver orders"
            ADMIN -> "Has full system access and management capabilities"
        }
    }
}
