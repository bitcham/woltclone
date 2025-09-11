package cham.woltclone.domain.user

enum class Permission {
    // Customer permissions
    PLACE_ORDER,
    VIEW_ORDER_HISTORY,
    CANCEL_ORDER,
    RATE_MERCHANT,
    
    // Merchant permissions
    MANAGE_MENU,
    VIEW_ORDERS,
    ACCEPT_ORDER,
    REJECT_ORDER,
    UPDATE_ORDER_STATUS,
    VIEW_ANALYTICS,
    
    // Courier permissions
    VIEW_DELIVERY_REQUESTS,
    ACCEPT_DELIVERY,
    UPDATE_DELIVERY_STATUS,
    VIEW_DELIVERY_HISTORY,
    
    // Admin permissions
    MANAGE_USERS,
    MANAGE_MERCHANTS,
    MANAGE_COURIERS,
    VIEW_SYSTEM_ANALYTICS,
    MODERATE_CONTENT,
    SYSTEM_CONFIGURATION
}