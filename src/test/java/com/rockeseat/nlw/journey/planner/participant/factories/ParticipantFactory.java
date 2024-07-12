package com.rockeseat.nlw.journey.planner.participant.factories;

import com.rockeseat.nlw.journey.planner.participant.Participant;
import com.rockeseat.nlw.journey.planner.participant.dtos.ParticipantRequestPayload;
import com.rockeseat.nlw.journey.planner.trip.Trip;

import java.util.ArrayList;
import java.util.List;

public class ParticipantFactory {

  public static Participant make(ParticipantRequestPayload payload, Trip trip) {
    return new Participant(payload.email(), trip);
  }

  public static List<Participant> makeMany(Trip trip) {
    var participants = new ArrayList<Participant>();
    participants.add(new Participant("participant1@mail.com", trip));
    participants.add(new Participant("participant2@mail.com", trip));

    return participants;
  }

}
