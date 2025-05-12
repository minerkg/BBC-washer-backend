package com.bcc.washer.domain;

import java.util.List;

public class TimeSlot {


    private TimeInterval timeInterval;
    private List<BookableUnit> bookableUnit;



    public TimeInterval getTimeSlot() {
        return timeInterval;
    }

    public void addNewBookableUnit(BookableUnit bookableUnit) {
        if (bookableUnit.get(bookableUnit.getTimeSlot()).isEmpty()) {
            this.bookableUnit.put(bookableUnit.getTimeSlot(), List.of(bookableUnit));
        } else {
            List<BookableUnit> existingUnits = bookableUnit.get(bookableUnit.getTimeSlot());
            existingUnits.add(bookableUnit);
            this.bookableUnit.put(bookableUnit.getTimeSlot(), existingUnits);
        }
    }

    //remove bookableUnits


}
