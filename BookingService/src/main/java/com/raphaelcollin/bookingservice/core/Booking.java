package com.raphaelcollin.bookingservice.core;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Booking {
    int id;
    int userId;
    LocalDate startDate;
    LocalDate endDate;
}
