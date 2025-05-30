package com.bcc.washer.service;


import com.bcc.washer.controller.ApiResponse;
import com.bcc.washer.domain.BookableUnit;
import com.bcc.washer.repository.BookableUnitRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class BookableUnitService {

    @Autowired
    private BookableUnitRepository bookableUnitRepository;


    public Set<BookableUnit> getAllAvailableBookableUnits() {

    }

    public Set<BookableUnit> getAllABookableUnitsHistoryByUser(Long userId) {
    }
}
