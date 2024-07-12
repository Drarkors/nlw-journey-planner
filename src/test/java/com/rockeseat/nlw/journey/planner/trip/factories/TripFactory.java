package com.rockeseat.nlw.journey.planner.trip.factories;

import com.rockeseat.nlw.journey.planner.trip.Trip;
import com.rockeseat.nlw.journey.planner.trip.dtos.TripRequestPayload;

import java.time.LocalDateTime;
import java.util.UUID;

public class TripFactory {
  public static final LocalDateTime DEFAULT_TRIP_STARTS_AT = LocalDateTime.now();
  public static final LocalDateTime DEFAULT_TRIP_ENDS_AT = LocalDateTime.now().plusDays(7L);

  public static Trip make(UUID id) {
    return new Trip(
        id,
        "Mocked destination",
        DEFAULT_TRIP_STARTS_AT,
        DEFAULT_TRIP_ENDS_AT,
        false,
        "owner@mail.com",
        "Owner");
  }

  public static Trip makeFromRequest(TripRequestPayload data) {
    return new Trip(data);
  }

}
