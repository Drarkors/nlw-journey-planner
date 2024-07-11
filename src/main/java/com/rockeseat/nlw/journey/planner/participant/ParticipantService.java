package com.rockeseat.nlw.journey.planner.participant;

import com.rockeseat.nlw.journey.planner.trip.Trip;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
public class ParticipantService {

  @Autowired
  private ParticipantRepository repository;

  public void registerParticipantsToEvent(List<String> participantsToInvite, Trip trip) {
    var participants = participantsToInvite.stream().map(email -> new Participant(email, trip)).toList();

    this.repository.saveAll(participants);

    participants.forEach(participant -> log.info("Participant: {}", participant.getId()));
  }

  public ParticipantCreateResponse registerParticipantToEvent(String email, Trip trip) {
    var participant = new Participant(email, trip);

    this.repository.save(participant);

    return new ParticipantCreateResponse(participant.getId());
  }

  public void triggerConfirmationEmailToParticipants(UUID tripId) {}

  public void triggerConfirmationEmailToParticipant(String email, Trip trip) {}

  public List<ParticipantData> getAllParticipantsFromTrip(Trip trip) {
    return this.repository.findByTripId(trip.getId()).stream().map(participant ->
        new ParticipantData(participant.getId(), participant.getName(),
            participant.getEmail(), participant.getIsConfirmed())
    ).toList();
  }

}
