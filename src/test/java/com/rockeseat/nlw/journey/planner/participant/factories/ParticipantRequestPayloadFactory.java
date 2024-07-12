package com.rockeseat.nlw.journey.planner.participant.factories;

import com.rockeseat.nlw.journey.planner.participant.dtos.ParticipantRequestPayload;

public class ParticipantRequestPayloadFactory {

  public static ParticipantRequestPayload make() {
    return new ParticipantRequestPayload("Participant", "participant@mail.com");
  }
}
