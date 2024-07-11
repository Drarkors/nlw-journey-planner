package com.rockeseat.nlw.journey.planner.trip.service;

import com.rockeseat.nlw.journey.planner.trip.Trip;
import com.rockeseat.nlw.journey.planner.trip.dtos.TripRequestPayload;
import com.rockeseat.nlw.journey.planner.trip.exceptions.TripInvalidStartDateException;
import com.rockeseat.nlw.journey.planner.trip.exceptions.TripNotFoundException;
import com.rockeseat.nlw.journey.planner.trip.repository.TripRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Service
public class TripService {

  @Autowired
  private TripRepository tripRepository;

  public Trip registerTrip(TripRequestPayload payload) {
    Trip trip = new Trip(payload);

    if (trip.getStartsAt().isAfter(trip.getEndsAt()))
      throw new TripInvalidStartDateException();

    return this.tripRepository.save(trip);
  }

  public Trip geTripFromId(UUID id) {
    return this.tripRepository.findById(id).orElseThrow(TripNotFoundException::new);
  }

  public Trip updateTrip(UUID id, TripRequestPayload payload) {
    var trip = this.tripRepository.findById(id).orElseThrow(TripNotFoundException::new);

    trip.setDestination(payload.destination());
    trip.setStartsAt(LocalDateTime.parse(payload.starts_at(), DateTimeFormatter.ISO_DATE_TIME));
    trip.setEndsAt(LocalDateTime.parse(payload.ends_at(), DateTimeFormatter.ISO_DATE_TIME));

    return this.tripRepository.save(trip);
  }

  public Trip confirmTrip(UUID id) {
    var trip = this.tripRepository.findById(id).orElseThrow(TripNotFoundException::new);

    trip.setIsConfirmed(true);

    return this.tripRepository.save(trip);
  }
}
