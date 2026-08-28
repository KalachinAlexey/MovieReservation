package com.cinema.contracts.notifications.v1;

public record ReservationNotification(
        Long id,
        String username,
        Long totalPrice,
        ReservationNotificationStatus status
) {}
