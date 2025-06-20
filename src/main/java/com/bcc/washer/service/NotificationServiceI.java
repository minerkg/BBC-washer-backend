package com.bcc.washer.service;


import com.bcc.washer.domain.BookableUnit;
import com.bcc.washer.domain.TemplateTYPE;
import com.bcc.washer.domain.reservation.Reservation;

public interface NotificationServiceI {
    void notifyReservation(String destination, String subject, String text, BookableUnit bookableUnit);
}

