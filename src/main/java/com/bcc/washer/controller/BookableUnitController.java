package com.bcc.washer.controller;


import com.bcc.washer.dto.BookableUnitDTO;
import com.bcc.washer.service.BookableUnitService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/bookable-units")
public class BookableUnitController {

    private Logger logger = LoggerFactory.getLogger(BookableUnitController.class);

    @Autowired
    private BookableUnitService bookableUnitService;

    //TODO: get all pagable method


    @GetMapping("")
    public ResponseEntity<ApiResponse<List<BookableUnitDTO>>> getAllAvailableBookableUnits() {
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

}
