package com.bcc.washer.controller;


import com.bcc.washer.domain.ResourceAlreadyExistsException;
import com.bcc.washer.dto.BookableUnitDto;
import com.bcc.washer.dto.BookableUnitDtoConverter;
import com.bcc.washer.service.BookableUnitService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

import java.util.HashSet;
import java.util.Set;

@RestController
@RequestMapping("/bookable-units")
public class BookableUnitController {

    private Logger logger = LoggerFactory.getLogger(BookableUnitController.class);

    @Autowired
    private BookableUnitService bookableUnitService;

    @Autowired
    private BookableUnitDtoConverter bookableUnitDtoConverter;


    //TODO: get all pagable method


    @GetMapping("")
    public ResponseEntity<ApiResponse<Set<BookableUnitDto>>> getAllAvailableBookableUnits() {
        try {
            logger.info("---  getAllBookableUnits method accessed  ---");
            var bookableUnits = bookableUnitService.getAllAvailableBookableUnitsWithinOneWeek();
            var bookableUnitDtoList = bookableUnitDtoConverter.convertModelListToDtoList(bookableUnits.stream().toList());
            return ResponseEntity.ok()
                    .body(
                            new ApiResponse<>("all bookable units",
                                    new HashSet<>(bookableUnitDtoList)
                            )
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



    @GetMapping("/by-date")
    public ResponseEntity<ApiResponse<Set<BookableUnitDto>>> getAllAvailableBookableUnitsByDay(@RequestParam LocalDate localDate) {
        try {
            logger.info("---  getAllBookableUnitsByDay method accessed  ---");
            var bookableUnits = bookableUnitService.getAllAvailableBookableUnitsWithinOneWeek(localDate);
            var bookableUnitDtoList = bookableUnitDtoConverter.convertModelListToDtoList(bookableUnits.stream().toList());
            return ResponseEntity.ok()
                    .body(
                            new ApiResponse<>("all bookable units by date:" + localDate ,
                                    new HashSet<>(bookableUnitDtoList)
                            )
                    );
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }


}
