package com.bcc.washer.controller;


import com.bcc.washer.domain.BookableUnit;
import com.bcc.washer.domain.ResourceAlreadyExistsException;
import com.bcc.washer.service.BookableUnitService;
import com.bcc.washer.service.ReservationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

@RestController
@RequestMapping("/bookable-units")
public class BookableUnitController {

    private Logger logger = LoggerFactory.getLogger(BookableUnitController.class);

    @Autowired
    private BookableUnitService bookableUnitService;

    //TODO: get all pagable method



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


    @PostMapping("/admin/generate-units")
    public ResponseEntity<ApiResponse<String>> generateBookableUnits() {
        try {
            bookableUnitService.generateBookableUnits();
            return ResponseEntity.ok().build();
        } catch (ResourceAlreadyExistsException e) {
            return ResponseEntity.status(HttpStatus.NOT_MODIFIED).build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }


}
