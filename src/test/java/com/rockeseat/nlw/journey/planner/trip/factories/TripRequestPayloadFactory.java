package com.rockeseat.nlw.journey.planner.trip.factories;

import com.rockeseat.nlw.journey.planner.trip.dtos.TripRequestPayload;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import static com.rockeseat.nlw.journey.planner.trip.factories.TripFactory.DEFAULT_TRIP_ENDS_AT;
import static com.rockeseat.nlw.journey.planner.trip.factories.TripFactory.DEFAULT_TRIP_STARTS_AT;

public class TripRequestPayloadFactory {

  public static TripRequestPayload make() {
    return new TripRequestPayload("Mocked destination",
        DEFAULT_TRIP_STARTS_AT.format(DateTimeFormatter.ISO_DATE_TIME),
        DEFAULT_TRIP_ENDS_AT.format(DateTimeFormatter.ISO_DATE_TIME),
        new ArrayList<>(),
        "owner@mail.com",
        "Owner");
  }

  public static TripRequestPayload make(String destination, String startsAt, String endsAt) {
    return new TripRequestPayload(destination,
        startsAt,
        endsAt,
        new ArrayList<>(),
        "owner@mail.com",
        "Owner");
  }

  public static TripRequestPayload makeWithInvalidStartDate() {
    return new TripRequestPayload("Mocked destination",
        DEFAULT_TRIP_ENDS_AT.plusDays(10L).format(DateTimeFormatter.ISO_DATE_TIME),
        DEFAULT_TRIP_ENDS_AT.format(DateTimeFormatter.ISO_DATE_TIME),
        new ArrayList<>(),
        "owner@mail.com",
        "Owner");
  }
}
