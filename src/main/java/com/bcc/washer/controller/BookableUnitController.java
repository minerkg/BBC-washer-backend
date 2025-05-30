package com.bcc.washer.controller;


import com.bcc.washer.domain.BookableUnit;
import com.bcc.washer.domain.Reservation;
import com.bcc.washer.service.BookableUnitService;
import com.bcc.washer.service.ReservationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/bookable-units")
public class BookableUnitController {

    private Logger logger = LoggerFactory.getLogger(BookableUnitController.class);

    @Autowired
    private BookableUnitService bookableUnitService;

    @GetMapping("")
    public ResponseEntity<ApiResponse<Set<BookableUnit>>> getAllAvailableBookableUnits() {
        try {
            logger.info("---  getAllBookableUnits method accessed  ---");
            return ResponseEntity.ok()
                    .body(new ApiResponse<>("all bookable units",
                            bookableUnitService.getAllAvailableBookableUnitsWithinOneWeek())
                    );
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

//    @GetMapping("/{userId}")
//    public ResponseEntity<ApiResponse<Set<BookableUnit>>> getAllBookableUnitsHistoryByUser(@PathVariable("userId") Long userId) {
//        try {
//            logger.info("get all bookable unit accessed");
//            return ResponseEntity.ok()
//                    .body(new ApiResponse<>("bookable unit history", bookableUnitService.getAllABookableUnitsHistoryByUser(userId))
//                    );
//        } catch (Exception e) {
//            return ResponseEntity.internalServerError().build();
//        }
//    }

    @GetMapping("/generate")
    public ResponseEntity<ApiResponse<String>> generateBookableUnits() {
        bookableUnitService.generateBookableUnits();
        return ResponseEntity.ok().build();
    }





}
