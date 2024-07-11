package com.rockeseat.nlw.journey.planner.trip.dtos;

import com.rockeseat.nlw.journey.planner.utils.RegexUtils;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.util.List;

public record TripRequestPayload(
    @NotNull(message = "Must be provided")
    @NotEmpty(message = "Must not be empty")
    @Size(min = 5, max = 255, message = "Must have from 5 to 255 characters")
    String destination,
    @NotNull(message = "Must be provided")
    @Pattern(regexp = RegexUtils.LOCALDATETIME_REGEX, message = "Must be a valid timestamp")
    String starts_at,
    @NotNull(message = "Must be provided")
    @Pattern(regexp = RegexUtils.LOCALDATETIME_REGEX, message = "Must be a valid timestamp")
    String ends_at,
    List<String> emails_to_invite,
    @NotNull(message = "Must be provided")
    @NotEmpty(message = "Must not be empty")
    @Email(message = "Must be a valid email")
    String owner_email,
    @NotNull(message = "Must be provided")
    @NotEmpty(message = "Must not be empty")
    @Size(min = 1, max = 255, message = "Must have from 1 to 255 characters")
    String owner_name) {
}
