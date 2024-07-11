package com.rockeseat.nlw.journey.planner.trip.exceptions;

public class TripNotFoundException extends RuntimeException {
  public TripNotFoundException() {
    super("Trip not found");
  }
}
