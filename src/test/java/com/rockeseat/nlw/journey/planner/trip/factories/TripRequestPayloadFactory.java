package com.rockeseat.nlw.journey.planner.trip.factories;

import com.rockeseat.nlw.journey.planner.trip.dtos.TripRequestPayload;

import java.util.ArrayList;

public class TripRequestPayloadFactory {

  public static TripRequestPayload make() {
    return new TripRequestPayload("Mocked destination",
        "2024-06-25T21:51:54.734Z",
        "2024-06-28T21:51:54.734Z",
        new ArrayList<>(),
        "owner@mail.com",
        "Owner");
  }

  public static TripRequestPayload makeWithInvalidStartDate() {
    return new TripRequestPayload("Mocked destination",
        "2024-06-28T21:51:54.734Z",
        "2024-06-25T21:51:54.734Z",
        new ArrayList<>(),
        "owner@mail.com",
        "Owner");
  }
}
