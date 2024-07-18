package com.rockeseat.nlw.journey.planner.participant.factories;

import com.rockeseat.nlw.journey.planner.participant.Participant;
import com.rockeseat.nlw.journey.planner.participant.dtos.ParticipantRequestPayload;
import com.rockeseat.nlw.journey.planner.participant.repository.ParticipantRepository;
import com.rockeseat.nlw.journey.planner.trip.Trip;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Lazy
@Component
public class ParticipantFactory {

  private final ParticipantRepository repository;

  public ParticipantFactory(ParticipantRepository repository) {
    this.repository = repository;
  }

  public static Participant fake(ParticipantRequestPayload payload, Trip trip) {
    return new Participant(payload.email(), trip);
  }

  public static List<Participant> fakeMany(Trip trip) {
    var participants = new ArrayList<Participant>();
    participants.add(new Participant("participant1@mail.com", trip));
    participants.add(new Participant("participant2@mail.com", trip));

    return participants;
  }

  public Participant makeUnconfirmed(Trip trip) {
    var participant = Participant.builder()
        .name("")
        .email("participant@mail.com")
        .isConfirmed(false)
        .trip(trip)
        .build();

    return this.repository.save(participant);
  }

}
