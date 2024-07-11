package com.rockeseat.nlw.journey.planner.trip.exceptions;

public class TripInvalidStartDateException extends RuntimeException {
  public TripInvalidStartDateException() {
    super("Trip [start_at] date is after it's [end_at] date");
  }
}
