package com.rockeseat.nlw.journey.planner.trip.exception;

public class TripInvalidStartDateException extends RuntimeException {
  public TripInvalidStartDateException() {
    super("Trip [start_at] date is after it's [end_at] date");
  }
}
