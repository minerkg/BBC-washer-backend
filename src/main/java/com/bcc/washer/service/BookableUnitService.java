package com.bcc.washer.service;


import com.bcc.washer.domain.BookableUnit;
import com.bcc.washer.repository.BookableUnitRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
public class BookableUnitService {

    @Autowired
    private BookableUnitRepository bookableUnitRepository;


    public Set<BookableUnit> getAllAvailableBookableUnits() {
        return bookableUnitRepository.findAll()
                .stream()
                .filter(BookableUnit::isAvailable)
                .collect(Collectors.toSet());

    }

    public Set<BookableUnit> getAllABookableUnitsHistoryByUser(Long userId) {
        //// not working yet

        return bookableUnitRepository.findAll()
                .stream()
                .filter(BookableUnit::isAvailable)
                .collect(Collectors.toSet());
    }
}
