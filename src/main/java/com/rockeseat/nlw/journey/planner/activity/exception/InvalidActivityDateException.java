package com.rockeseat.nlw.journey.planner.activity.exception;

public class InvalidActivityDateException extends RuntimeException {
  public InvalidActivityDateException() {
    super("Activity should occur between it's Trip [starts_at] and [ends_at] dates");
  }
}
