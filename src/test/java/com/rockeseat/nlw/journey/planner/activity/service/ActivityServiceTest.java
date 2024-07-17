package com.rockeseat.nlw.journey.planner.activity.service;

import com.rockeseat.nlw.journey.planner.activity.Activity;
import com.rockeseat.nlw.journey.planner.activity.exception.InvalidActivityDateException;
import com.rockeseat.nlw.journey.planner.activity.factories.ActivityFactory;
import com.rockeseat.nlw.journey.planner.activity.factories.ActivityRequestPayloadFactory;
import com.rockeseat.nlw.journey.planner.activity.repository.ActivityRepository;
import com.rockeseat.nlw.journey.planner.trip.factories.TripFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.IntStream;

import static com.rockeseat.nlw.journey.planner.trip.factories.TripFactory.DEFAULT_TRIP_ENDS_AT;
import static com.rockeseat.nlw.journey.planner.trip.factories.TripFactory.DEFAULT_TRIP_STARTS_AT;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ActivityServiceTest {

  @Mock
  private ActivityRepository repository;

  @InjectMocks
  private ActivityService service;

  private static List<String> shouldRegisterActivitySource() {
    var dates = new ArrayList<String>();
    dates.add(DEFAULT_TRIP_STARTS_AT.format(DateTimeFormatter.ISO_DATE_TIME));
    dates.add(DEFAULT_TRIP_ENDS_AT.format(DateTimeFormatter.ISO_DATE_TIME));

    return dates;
  }

  @ParameterizedTest
  @MethodSource("shouldRegisterActivitySource")
  void registerActivity_shouldRegisterActivityToEvent(String occursAt) {
    var trip = TripFactory.fake(UUID.randomUUID());
    var payload = ActivityRequestPayloadFactory.make(occursAt);

    when(this.repository.save(any(Activity.class))).thenAnswer((invocation -> invocation.getArgument(0)));

    var activity = this.service.registerActivity(payload, trip);

    assertEquals(payload.title(), activity.getTitle());
    assertEquals(payload.occurs_at(), activity.getOccursAt().format(DateTimeFormatter.ISO_DATE_TIME));
  }

  @Test
  void registerActivity_shouldThrowInvalidActivityDateException() {
    var trip = TripFactory.fake(UUID.randomUUID());

    var beforeStartsAt = DEFAULT_TRIP_STARTS_AT.minusDays(2L).format(DateTimeFormatter.ISO_DATE_TIME);
    var startsAtPayload = ActivityRequestPayloadFactory.make(beforeStartsAt);
    assertThrows(InvalidActivityDateException.class, () -> this.service.registerActivity(startsAtPayload, trip));

    var afterEndsAt = DEFAULT_TRIP_ENDS_AT.plusDays(2L).format(DateTimeFormatter.ISO_DATE_TIME);
    var endsAtPayload = ActivityRequestPayloadFactory.make(afterEndsAt);
    assertThrows(InvalidActivityDateException.class, () -> this.service.registerActivity(endsAtPayload, trip));
  }

  @Test
  void getAllActivitiesFromTripId_shouldReturnActivities() {
    var trip = TripFactory.fake(UUID.randomUUID());
    var expectedActivies = ActivityFactory.makeMany(trip);

    when(this.repository.findByTripId(eq(trip.getId()))).thenReturn(expectedActivies);

    var activities = this.service.getAllActivitiesFromTripId(trip.getId());

    assertTrue(IntStream.range(0, expectedActivies.size()).allMatch(i ->
        expectedActivies.get(i).getTitle().equals(activities.get(i).title())
            && expectedActivies.get(i).getOccursAt().equals(activities.get(i).occurs_at())));
  }

}
