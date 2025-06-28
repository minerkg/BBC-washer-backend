package com.bcc.washer.service;


import com.bcc.washer.domain.BookableUnit;

public interface NotificationServiceI {
    void notifyReservation(String destination, String subject, String text, BookableUnit bookableUnit);

    void notifyCancellation(String destination, String subject, String text, BookableUnit bookableUnit);
}

