package com.rockeseat.nlw.journey.planner.activity.factories;

import com.rockeseat.nlw.journey.planner.activity.Activity;
import com.rockeseat.nlw.journey.planner.trip.Trip;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static com.rockeseat.nlw.journey.planner.trip.factories.TripFactory.DEFAULT_TRIP_STARTS_AT;

public class ActivityFactory {
  public static List<Activity> makeMany(Trip trip) {
    var activities = new ArrayList<Activity>();
    activities.add(new Activity("Title 1",
        DEFAULT_TRIP_STARTS_AT.format(DateTimeFormatter.ISO_DATE_TIME), trip));
    activities.add(new Activity("Title 2",
        DEFAULT_TRIP_STARTS_AT.plusHours(1).format(DateTimeFormatter.ISO_DATE_TIME), trip));

    return activities;
  }
}
