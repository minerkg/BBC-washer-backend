package com.bcc.washer.service;

import com.bcc.washer.domain.time.TimeInterval;
import com.bcc.washer.repository.TimeIntervalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalTime;

@Service
public class TimeIntervalService {

    @Autowired
    private TimeIntervalRepository timeIntervalRepository;


    public TimeInterval getTimeIntervalBetweenTime(LocalTime start, LocalTime end){
       return timeIntervalRepository.findByStartTimeAndEndTime(start, end);
    }
}
