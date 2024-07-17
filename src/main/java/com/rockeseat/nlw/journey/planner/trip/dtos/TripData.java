package com.rockeseat.nlw.journey.planner.trip.dtos;

import lombok.Builder;

import java.util.UUID;

@Builder
public record TripData(UUID id, String destination, String starts_at, String ends_at,
                       Boolean is_confirmed, String owner_email, String owner_name) {
}
