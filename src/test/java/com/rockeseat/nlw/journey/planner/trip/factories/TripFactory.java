package com.rockeseat.nlw.journey.planner.trip.factories;

import com.rockeseat.nlw.journey.planner.trip.Trip;
import com.rockeseat.nlw.journey.planner.trip.dtos.TripRequestPayload;
import com.rockeseat.nlw.journey.planner.trip.repository.TripRepository;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.UUID;

@Lazy
@Component
public class TripFactory {
  public static final LocalDateTime DEFAULT_TRIP_STARTS_AT = LocalDateTime.now();
  public static final LocalDateTime DEFAULT_TRIP_ENDS_AT = LocalDateTime.now().plusDays(7L);

  private final TripRepository tripRepository;

  public TripFactory(TripRepository tripRepository) {
    this.tripRepository = tripRepository;
  }

  public static Trip fake(UUID id) {
    return new Trip(
        id,
        "Mocked destination",
        DEFAULT_TRIP_STARTS_AT,
        DEFAULT_TRIP_ENDS_AT,
        false,
        "owner@mail.com",
        "Owner");
  }

  public static Trip fakeFromRequest(TripRequestPayload data) {
    return new Trip(data);
  }

  public Trip make() {
    var trip = Trip.builder()
        .destination("Mocked destination")
        .startsAt(DEFAULT_TRIP_STARTS_AT)
        .endsAt(DEFAULT_TRIP_ENDS_AT)
        .isConfirmed(false)
        .ownerEmail("owner@mail.com")
        .ownerName("Owner")
        .build();


    return this.tripRepository.save(trip);
  }

}
