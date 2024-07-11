package com.rockeseat.nlw.journey.planner.activity;

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

    this.repository.save(activity);

    return new ActivityCreateResponse(activity.getId());
  }

  public List<ActivityData> getAllParticipantsFromTripId(UUID tripId) {
    return this.repository.findByTripId(tripId).stream().map(activity ->
        new ActivityData(activity.getId(), activity.getTitle(), activity.getOccursAt())
    ).toList();
  }

}
