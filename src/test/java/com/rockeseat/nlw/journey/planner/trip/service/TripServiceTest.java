package com.rockeseat.nlw.journey.planner.trip.service;

import com.rockeseat.nlw.journey.planner.participant.service.ParticipantService;
import com.rockeseat.nlw.journey.planner.trip.Trip;
import com.rockeseat.nlw.journey.planner.trip.exceptions.TripInvalidStartDateException;
import com.rockeseat.nlw.journey.planner.trip.exceptions.TripNotFoundException;
import com.rockeseat.nlw.journey.planner.trip.factories.TripFactory;
import com.rockeseat.nlw.journey.planner.trip.factories.TripRequestPayloadFactory;
import com.rockeseat.nlw.journey.planner.trip.repository.TripRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TripServiceTest {

  @Mock
  private TripRepository tripRepository;

  @Mock
  private ParticipantService participantService;

  @InjectMocks
  private TripService tripService;

  @Test
  void registerTrip_shouldRegisterATrip() {
    var payload = TripRequestPayloadFactory.make();
    var expected = TripFactory.fakeFromRequest(payload);
    var expectedId = UUID.randomUUID();

    when(tripRepository.save(eq(expected))).thenAnswer((t) -> {
      expected.setId(expectedId);
      return expected;
    });

    var response = this.tripService.registerTrip(payload);

    assertEquals(expectedId, response.getId());

    verify(this.tripRepository, times(1)).save(any(Trip.class));
  }

  @Test
  void registerTrip_shouldThrowTripInvalidStartDateException() {
    var payload = TripRequestPayloadFactory.makeWithInvalidStartDate();

    assertThrows(TripInvalidStartDateException.class, () -> this.tripService.registerTrip(payload));

    verify(this.tripRepository, times(0)).save(any(Trip.class));
  }

  @Test
  void getTripFromId_shouldFindAndReturnATripById() {
    var expectedId = UUID.randomUUID();
    var expected = TripFactory.fake(expectedId);

    when(tripRepository.findById(eq(expectedId))).thenReturn(Optional.of(expected));

    var response = this.tripService.geTripFromId(expectedId);

    assertEquals(expectedId, response.getId());

    verify(this.tripRepository, times(1)).findById(eq(expectedId));
  }

  @Test
  void getTripFromId_shouldThrowTripNotFoundException() {
    var expectedId = UUID.randomUUID();

    when(this.tripRepository.findById(eq(expectedId))).thenReturn(Optional.empty());

    assertThrows(TripNotFoundException.class, () -> this.tripService.geTripFromId(expectedId));

    verify(this.tripRepository, times(1)).findById(eq(expectedId));
  }

  @Test
  void updateTrip_shouldUpdateATrip() {
    var payload = TripRequestPayloadFactory.make(
        "New destination",
        LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME),
        LocalDateTime.now().plusDays(2L).format(DateTimeFormatter.ISO_DATE_TIME)
    );

    var expectedId = UUID.randomUUID();
    var expected = TripFactory.fake(expectedId);

    when(tripRepository.findById(eq(expectedId))).thenReturn(Optional.of(expected));
    when(tripRepository.save(eq(expected))).thenAnswer((invocationOnMock -> invocationOnMock.getArgument(0)));

    var response = this.tripService.updateTrip(expectedId, payload);

    assertEquals(expectedId, response.getId());

    assertEquals(payload.destination(), response.getDestination());
    assertEquals(payload.starts_at(), response.getStartsAt().format(DateTimeFormatter.ISO_DATE_TIME));
    assertEquals(payload.ends_at(), response.getEndsAt().format(DateTimeFormatter.ISO_DATE_TIME));

    verify(this.tripRepository, times(1)).findById(eq(expectedId));
    verify(this.tripRepository, times(1)).save(any(Trip.class));
  }

  @Test
  void updateTrip_shouldThrowTripNotFoundException() {
    var payload = TripRequestPayloadFactory.make();
    var expectedId = UUID.randomUUID();

    when(this.tripRepository.findById(eq(expectedId))).thenReturn(Optional.empty());

    assertThrows(TripNotFoundException.class, () -> this.tripService.updateTrip(expectedId, payload));

    verify(this.tripRepository, times(1)).findById(eq(expectedId));
    verify(this.tripRepository, times(0)).save(any(Trip.class));
  }

  @Test
  void confirmTrip_shouldUpdateATrip() {
    var expectedId = UUID.randomUUID();
    var expected = TripFactory.fake(expectedId);

    when(tripRepository.findById(eq(expectedId))).thenReturn(Optional.of(expected));
    when(tripRepository.save(eq(expected))).thenAnswer((invocation) -> invocation.getArgument(0));

    var response = this.tripService.confirmTrip(expectedId);

    assertEquals(expectedId, response.getId());
    assertEquals(true, response.getIsConfirmed());

    verify(this.tripRepository, times(1)).findById(eq(expectedId));
    verify(this.tripRepository, times(1)).save(any(Trip.class));
  }

  @Test
  void confirmTrip_shouldThrowTripNotFoundException() {
    var expectedId = UUID.randomUUID();

    when(this.tripRepository.findById(eq(expectedId))).thenReturn(Optional.empty());

    assertThrows(TripNotFoundException.class, () -> this.tripService.confirmTrip(expectedId));

    verify(this.tripRepository, times(1)).findById(eq(expectedId));
    verify(this.tripRepository, times(0)).save(any(Trip.class));
  }

}
