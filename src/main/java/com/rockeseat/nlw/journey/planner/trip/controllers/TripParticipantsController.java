package com.rockeseat.nlw.journey.planner.trip.controllers;

import com.rockeseat.nlw.journey.planner.participant.dtos.ParticipantCreateResponse;
import com.rockeseat.nlw.journey.planner.participant.dtos.ParticipantData;
import com.rockeseat.nlw.journey.planner.participant.dtos.ParticipantRequestPayload;
import com.rockeseat.nlw.journey.planner.participant.service.ParticipantService;
import com.rockeseat.nlw.journey.planner.trip.service.TripService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/trips")
public class TripParticipantsController {

  @Autowired
  private TripService tripService;

  @Autowired
  private ParticipantService participantService;

  @PostMapping("/{id}/invite")
  public ResponseEntity<ParticipantCreateResponse> inviteParticipant(@PathVariable UUID id, @RequestBody ParticipantRequestPayload payload) {
    var trip = this.tripService.geTripFromId(id);

    var participant = this.participantService.registerParticipantToEvent(payload.email(), trip);

    return ResponseEntity.ok(new ParticipantCreateResponse(participant.getId()));
  }


  @GetMapping("/{id}/participants")
  public ResponseEntity<List<ParticipantData>> findTripParticipants(@PathVariable UUID id) {
    var trip = this.tripService.geTripFromId(id);

    var participants = this.participantService.getAllParticipantsFromTrip(trip);

    return ResponseEntity.ok(participants);
  }

}
