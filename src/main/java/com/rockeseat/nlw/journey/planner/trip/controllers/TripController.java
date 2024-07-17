package com.rockeseat.nlw.journey.planner.trip.controllers;

import com.rockeseat.nlw.journey.planner.participant.service.ParticipantService;
import com.rockeseat.nlw.journey.planner.trip.Trip;
import com.rockeseat.nlw.journey.planner.trip.dtos.TripCreateResponse;
import com.rockeseat.nlw.journey.planner.trip.dtos.TripData;
import com.rockeseat.nlw.journey.planner.trip.dtos.TripRequestPayload;
import com.rockeseat.nlw.journey.planner.trip.service.TripService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.format.DateTimeFormatter;
import java.util.UUID;

@RestController
@RequestMapping("/trips")
public class TripController {

  @Autowired
  private TripService tripService;

  @Autowired
  private ParticipantService participantService;

  @PostMapping
  public ResponseEntity<TripCreateResponse> createTrip(@RequestBody @Valid TripRequestPayload payload) {
    var trip = this.tripService.registerTrip(payload);

    this.participantService.registerParticipantsToEvent(payload.emails_to_invite(), trip);

    return ResponseEntity.status(HttpStatus.CREATED).body(new TripCreateResponse(trip.getId()));
  }

  @GetMapping("/{id}")
  public ResponseEntity<TripData> getTripDetails(@PathVariable UUID id) {
    var trip = this.tripService.geTripFromId(id);

    return ResponseEntity.ok(TripData.builder()
        .id(trip.getId())
        .destination(trip.getDestination())
        .starts_at(trip.getStartsAt().format(DateTimeFormatter.ISO_DATE_TIME))
        .ends_at(trip.getEndsAt().format(DateTimeFormatter.ISO_DATE_TIME))
        .is_confirmed(trip.getIsConfirmed())
        .owner_name(trip.getOwnerName())
        .owner_email(trip.getOwnerEmail())
        .build());
  }

  @PutMapping("/{id}")
  public ResponseEntity<Trip> updateTrip(@PathVariable UUID id, @RequestBody TripRequestPayload payload) {
    var trip = this.tripService.updateTrip(id, payload);

    return ResponseEntity.ok(trip);
  }

  @GetMapping("/{id}/confirm")
  public ResponseEntity<Trip> confirmTrip(@PathVariable UUID id) {
    var trip = this.tripService.confirmTrip(id);

    this.participantService.triggerConfirmationEmailToParticipants(id);

    return ResponseEntity.ok(trip);
  }

}
