package com.rockeseat.nlw.journey.planner.trip.controllers;

import com.rockeseat.nlw.journey.planner.link.dtos.LinkCreateResponse;
import com.rockeseat.nlw.journey.planner.link.dtos.LinkData;
import com.rockeseat.nlw.journey.planner.link.dtos.LinkRequestPayload;
import com.rockeseat.nlw.journey.planner.link.service.LinkService;
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
public class TripLinksController {
  @Autowired
  private TripService tripService;
  @Autowired
  private LinkService linkService;

  @PostMapping("/{id}/links")
  public ResponseEntity<LinkCreateResponse> registerLink(@PathVariable UUID id, @RequestBody LinkRequestPayload payload) {
    var trip = this.tripService.geTripFromId(id);

    var link = this.linkService.registerLink(payload, trip);

    return ResponseEntity.ok(link);
  }

  @GetMapping("/{id}/links")
  public ResponseEntity<List<LinkData>> getAllLinks(@PathVariable UUID id) {
    var trip = this.tripService.geTripFromId(id);

    var links = this.linkService.getAllParticipantsFromTripId(trip.getId());

    return ResponseEntity.ok(links);
  }
}
