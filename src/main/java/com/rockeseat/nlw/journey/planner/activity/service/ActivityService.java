package com.rockeseat.nlw.journey.planner.activity.service;

import com.rockeseat.nlw.journey.planner.activity.Activity;
import com.rockeseat.nlw.journey.planner.activity.dtos.ActivityCreateResponse;
import com.rockeseat.nlw.journey.planner.activity.dtos.ActivityData;
import com.rockeseat.nlw.journey.planner.activity.dtos.ActivityRequestPayload;
import com.rockeseat.nlw.journey.planner.activity.exception.InvalidActivityDateException;
import com.rockeseat.nlw.journey.planner.activity.repository.ActivityRepository;
import com.rockeseat.nlw.journey.planner.trip.Trip;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ActivityService {

  @Autowired
  private ActivityRepository repository;

  public ActivityCreateResponse registerActivity(ActivityRequestPayload payload, Trip trip) {
    var activity = new Activity(payload.title(), payload.occurs_at(), trip);

    var isActivityOccurAtValid = activity.getOccursAt().isBefore(trip.getEndsAt()) &&
        (activity.getOccursAt().isEqual(trip.getStartsAt()) || activity.getOccursAt().isAfter(trip.getStartsAt()));

    if (!isActivityOccurAtValid)
      throw new InvalidActivityDateException();

    this.repository.save(activity);

    return new ActivityCreateResponse(activity.getId());
  }

  public List<ActivityData> getAllParticipantsFromTripId(UUID tripId) {
    return this.repository.findByTripId(tripId).stream().map(activity ->
        new ActivityData(activity.getId(), activity.getTitle(), activity.getOccursAt())
    ).toList();
  }

}
