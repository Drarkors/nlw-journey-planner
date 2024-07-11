package com.rockeseat.nlw.journey.planner.trip.exception;

public class TripNotFoundException extends RuntimeException {
  public TripNotFoundException() {
    super("Trip not found");
  }
}
