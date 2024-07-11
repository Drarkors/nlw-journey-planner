package com.rockeseat.nlw.journey.planner.participant.exception;

public class ParticipantNotFoundException extends RuntimeException {
  public ParticipantNotFoundException() {
    super("Participant not found");
  }
}
