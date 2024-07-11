package com.rockeseat.nlw.journey.planner.trip.repository;

import com.rockeseat.nlw.journey.planner.trip.Trip;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface TripRepository extends JpaRepository<Trip, UUID> {
}
