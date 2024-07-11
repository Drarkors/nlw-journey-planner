package com.rockeseat.nlw.journey.planner.trip.controllers;

import com.rockeseat.nlw.journey.planner.activity.dtos.ActivityCreateResponse;
import com.rockeseat.nlw.journey.planner.activity.dtos.ActivityData;
import com.rockeseat.nlw.journey.planner.activity.dtos.ActivityRequestPayload;
import com.rockeseat.nlw.journey.planner.activity.service.ActivityService;
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
public class TripActivitiesController {

  @Autowired
  private TripService tripService;

  @Autowired
  private ActivityService activityService;

  @PostMapping("/{id}/activities")
  public ResponseEntity<ActivityCreateResponse> registerActivity(@PathVariable UUID id, @RequestBody ActivityRequestPayload payload) {
    var trip = this.tripService.geTripFromId(id);

    var response = this.activityService.registerActivity(payload, trip);

    return ResponseEntity.ok(response);
  }

  @GetMapping("/{id}/activities")
  public ResponseEntity<List<ActivityData>> getAllActivities(@PathVariable UUID id) {
    var trip = this.tripService.geTripFromId(id);

    var activities = this.activityService.getAllParticipantsFromTripId(trip.getId());

    return ResponseEntity.ok(activities);
  }
}
