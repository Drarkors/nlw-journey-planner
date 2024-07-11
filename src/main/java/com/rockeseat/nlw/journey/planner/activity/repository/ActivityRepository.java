package com.rockeseat.nlw.journey.planner.activity.repository;

import com.rockeseat.nlw.journey.planner.activity.Activity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ActivityRepository extends JpaRepository<Activity, UUID> {
  List<Activity> findByTripId(UUID tripId);
}
