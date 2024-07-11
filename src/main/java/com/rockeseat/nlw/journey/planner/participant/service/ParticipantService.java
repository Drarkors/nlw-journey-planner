package com.rockeseat.nlw.journey.planner.participant.service;

import com.rockeseat.nlw.journey.planner.participant.Participant;
import com.rockeseat.nlw.journey.planner.participant.dtos.ParticipantData;
import com.rockeseat.nlw.journey.planner.participant.dtos.ParticipantRequestPayload;
import com.rockeseat.nlw.journey.planner.participant.exception.ParticipantNotFoundException;
import com.rockeseat.nlw.journey.planner.participant.repository.ParticipantRepository;
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

  public Participant confirmParticipant(UUID id, ParticipantRequestPayload payload) {
    var participant = this.repository.findById(id).orElseThrow(ParticipantNotFoundException::new);

    participant.setIsConfirmed(true);
    participant.setName(payload.name());
    participant.setEmail(payload.email());

    return this.repository.save(participant);
  }

  public void registerParticipantsToEvent(List<String> participantsToInvite, Trip trip) {
    var participants = participantsToInvite.stream().map(email -> new Participant(email, trip)).toList();

    this.repository.saveAll(participants);

    participants.forEach(participant -> log.info("Participant: {}", participant.getId()));
  }

  public Participant registerParticipantToEvent(String email, Trip trip) {
    var participant = new Participant(email, trip);

    this.repository.save(participant);

    if (trip.getIsConfirmed())
      triggerConfirmationEmailToParticipant(email, trip);

    return participant;
  }

  public void triggerConfirmationEmailToParticipants(UUID tripId) {
  }

  private void triggerConfirmationEmailToParticipant(String email, Trip trip) {
  }

  public List<ParticipantData> getAllParticipantsFromTrip(Trip trip) {
    return this.repository.findByTripId(trip.getId()).stream().map(participant ->
        new ParticipantData(participant.getId(), participant.getName(),
            participant.getEmail(), participant.getIsConfirmed())
    ).toList();
  }

}
