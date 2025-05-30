// src/main/java/com/bcc/washer/service/TimeManagementService.java (Renamed file and class)
package com.bcc.washer.service;

import com.bcc.washer.domain.TimeInterval;
import com.bcc.washer.domain.TimeSlot;
import com.bcc.washer.repository.TimeIntervalRepository;
import com.bcc.washer.repository.TimeSlotRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional; // Import this

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Service // Changed from @Repository to @Service, if it was @Repository
public class TimeManagementService { // Renamed from TimeSlotManager

    @Autowired
    private TimeSlotRepository timeSlotRepository;

    @Autowired
    private TimeIntervalRepository timeIntervalRepository;

    public List<TimeSlot> getAvailableTimeSlots() {
        // This method can be enhanced later to filter based on actual availability
        // or for specific criteria. For now, it returns all.
        return timeSlotRepository.findAll();
    }

    /**
     * Generates 1-hour time intervals for a given date, from 00:00 to 23:00.
     * @param date The date for which to generate time intervals.
     * @return A list of generated TimeIntervals.
     */
    @Transactional
    public List<TimeInterval> generateDailyTimeIntervals(LocalDate date) {
        List<TimeInterval> intervals = new ArrayList<>();
        // Assuming 1-hour slots from 00:00 to 23:00
        for (int hour = 0; hour < 24; hour++) {
            LocalDateTime startTime = LocalDateTime.of(date, LocalTime.of(hour, 0));
            LocalDateTime endTime = LocalDateTime.of(date, LocalTime.of(hour, 59, 59)); // End at 59th minute, 59th second

            TimeInterval newInterval = TimeInterval.builder()
                    .startTime(startTime)
                    .endTime(endTime)
                    .date(date)
                    .build();
            intervals.add(newInterval);
        }
        return timeIntervalRepository.saveAll(intervals);
    }

    public TimeInterval saveTimeInterval(TimeInterval timeInterval) {
        // Add validation here if needed, e.g., ensure start time is before end time
        return timeIntervalRepository.save(timeInterval);
    }

    public List<TimeInterval> getAllTimeIntervals() {
        return timeIntervalRepository.findAll();
    }

    public TimeInterval getTimeIntervalById(Long id) {
        return timeIntervalRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("TimeInterval not found with ID: " + id));
    }

    public void deleteTimeInterval(Long id) {
        if (!timeIntervalRepository.existsById(id)) {
            throw new RuntimeException("TimeInterval not found with ID: " + id);
        }
        timeIntervalRepository.deleteById(id);
    }
}