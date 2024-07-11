package com.rockeseat.nlw.journey.planner.trip.factory;

import com.rockeseat.nlw.journey.planner.trip.Trip;
import com.rockeseat.nlw.journey.planner.trip.dtos.TripRequestPayload;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

public class TripFactory {
  public static Trip make(UUID id) {
    return new Trip(
        id,
        "Mocked destination",
        LocalDateTime.parse("2024-06-25T21:51:54.734Z", DateTimeFormatter.ISO_DATE_TIME),
        LocalDateTime.parse("2024-06-28T21:51:54.734Z", DateTimeFormatter.ISO_DATE_TIME),
        false,
        "owner@mail.com",
        "Owner");
  }

  public static Trip makeFromRequest(TripRequestPayload data) {
    return new Trip(data);
  }
  
}
