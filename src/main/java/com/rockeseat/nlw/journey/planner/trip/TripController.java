package com.rockeseat.nlw.journey.planner.trip;

import com.rockeseat.nlw.journey.planner.participant.ParticipantCreateResponse;
import com.rockeseat.nlw.journey.planner.participant.ParticipantData;
import com.rockeseat.nlw.journey.planner.participant.ParticipantRequestPayload;
import com.rockeseat.nlw.journey.planner.participant.ParticipantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/trips")
public class TripController {

  @Autowired
  private ParticipantService participantService;

  @Autowired
  private TripRepository tripRepository;

  @PostMapping
  public ResponseEntity<TripCreateResponse> createTrip(@RequestBody TripRequestPayload payload) {
    Trip newTrip = new Trip(payload);

    this.tripRepository.save(newTrip);

    this.participantService.registerParticipantsToEvent(payload.emails_to_invite(), newTrip);

    return ResponseEntity.ok(new TripCreateResponse(newTrip.getId()));
  }

  @GetMapping("/{id}")
  public ResponseEntity<Trip> getTripDetails(@PathVariable UUID id) {
    Optional<Trip> trip = this.tripRepository.findById(id);

    return trip.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
  }

  @PutMapping("/{id}")
  public ResponseEntity<Trip> updateTrip(@PathVariable UUID id, @RequestBody TripRequestPayload payload) {
    Optional<Trip> trip = this.tripRepository.findById(id);

    if (trip.isPresent()) {
      Trip rawTrip = trip.get();
      rawTrip.setDestination(payload.destination());
      rawTrip.setStartsAt(LocalDateTime.parse(payload.starts_at(), DateTimeFormatter.ISO_DATE_TIME));
      rawTrip.setEndsAt(LocalDateTime.parse(payload.ends_at(), DateTimeFormatter.ISO_DATE_TIME));

      this.tripRepository.save(rawTrip);

      return ResponseEntity.ok(rawTrip);
    }

    return ResponseEntity.notFound().build();
  }

  @GetMapping("/{id}/confirm")
  public ResponseEntity<Trip> confirmTrip(@PathVariable UUID id) {
    Optional<Trip> trip = this.tripRepository.findById(id);

    if (trip.isPresent()) {
      Trip rawTrip = trip.get();
      rawTrip.setIsConfirmed(true);

      this.tripRepository.save(rawTrip);
      this.participantService.triggerConfirmationEmailToParticipants(id);

      return ResponseEntity.ok(rawTrip);
    }

    return ResponseEntity.notFound().build();
  }

  @PostMapping("/{id}/invite")
  public ResponseEntity<ParticipantCreateResponse> inviteParticipant(@PathVariable UUID id, @RequestBody ParticipantRequestPayload payload) {
    Optional<Trip> trip = this.tripRepository.findById(id);

    if (trip.isPresent()) {
      Trip rawTrip = trip.get();

      ParticipantCreateResponse response = this.participantService.registerParticipantToEvent(payload.email(), rawTrip);

      if (rawTrip.getIsConfirmed()) {
        this.participantService.triggerConfirmationEmailToParticipant(payload.email(), rawTrip);
      }

      return ResponseEntity.ok(response);
    }

    return ResponseEntity.notFound().build();
  }


  @GetMapping("/{id}/participants")
  public ResponseEntity<List<ParticipantData>> findTripParticipants(@PathVariable UUID id) {
    Optional<Trip> trip = this.tripRepository.findById(id);

    if (trip.isPresent()) {
      Trip rawTrip = trip.get();

      var participants = this.participantService.getAllParticipantsFromTrip(rawTrip);

      return ResponseEntity.ok(participants);
    }

    return ResponseEntity.notFound().build();
  }

}
