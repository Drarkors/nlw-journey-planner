package com.rockeseat.nlw.journey.planner.participant;

import java.util.UUID;

public record ParticipantData(UUID id, String name, String email, Boolean isConfirmed) {
}
