package com.rockeseat.nlw.journey.planner.participant.service;

import com.rockeseat.nlw.journey.planner.participant.Participant;
import com.rockeseat.nlw.journey.planner.participant.exception.ParticipantNotFoundException;
import com.rockeseat.nlw.journey.planner.participant.factories.ParticipantFactory;
import com.rockeseat.nlw.journey.planner.participant.factories.ParticipantRequestPayloadFactory;
import com.rockeseat.nlw.journey.planner.participant.repository.ParticipantRepository;
import com.rockeseat.nlw.journey.planner.trip.factories.TripFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ParticipantServiceTest {

  @Mock
  private ParticipantRepository repository;

  @InjectMocks
  private ParticipantService service;


  @ParameterizedTest
  @ValueSource(strings = {"participant1@mail.com", "participant2@mail.com;participant3@mail.com"})
  void registerParticipantsToEvent_shouldRegisterParticipantsToEvent(String emails) {
    var trip = TripFactory.make(UUID.randomUUID());

    var participantsEmail = emails.split(";");

    when(this.repository.saveAll(anyList())).thenAnswer((invocation) -> Arrays.stream(participantsEmail)
        .map(email -> new Participant(email, trip)).collect(Collectors.toList()));


    this.service.registerParticipantsToEvent(List.of(participantsEmail), trip);

    ArgumentCaptor<List<Participant>> captor = ArgumentCaptor.captor();

    verify(this.repository).saveAll(captor.capture());

    captor.getValue().forEach(capturedParticipant -> assertEquals(trip, capturedParticipant.getTrip()));
  }

  @ParameterizedTest
  @ValueSource(booleans = {false, true})
  void registerParticipantToEvent_shouldRegisterAParticipantToEvent(boolean isConfirmed) {
    var trip = TripFactory.make(UUID.randomUUID());
    trip.setIsConfirmed(isConfirmed);

    var payload = ParticipantRequestPayloadFactory.make();
    var expectedParticipant = ParticipantFactory.make(payload, trip);

    when(repository.save(eq(expectedParticipant))).thenReturn(expectedParticipant);

    var participant = this.service.registerParticipantToEvent(payload.email(), trip);

    assertEquals(expectedParticipant, participant);
  }

  @Test
  void confirmParticipant_shouldConfirmAParticipant() {
    var trip = TripFactory.make(UUID.randomUUID());
    var payload = ParticipantRequestPayloadFactory.make();

    var expectedId = UUID.randomUUID();
    var expectedParticipant = ParticipantFactory.make(payload, trip);
    expectedParticipant.setEmail("wrong@email.com");
    expectedParticipant.setId(expectedId);

    when(this.repository.findById(eq(expectedId))).thenReturn(Optional.of(expectedParticipant));
    when(this.repository.save(any(Participant.class))).thenAnswer((arg) -> arg.getArgument(0));

    var participant = this.service.confirmParticipant(expectedId, payload);

    assertEquals(true, participant.getIsConfirmed());
    assertEquals(payload.name(), participant.getName());
    assertEquals(payload.email(), participant.getEmail());
  }

  @Test
  void confirmParticipant_shouldThrowParticipantNotFoundException() {
    var payload = ParticipantRequestPayloadFactory.make();

    var expectedId = UUID.randomUUID();


    when(this.repository.findById(eq(expectedId))).thenReturn(Optional.empty());

    assertThrows(ParticipantNotFoundException.class, () -> this.service.confirmParticipant(expectedId, payload));

    verify(this.repository, times(0)).save(any(Participant.class));
  }

  @Test
  void getAllParticipantsFromTrip_shouldFindAllParticipantFromEvent() {
    var trip = TripFactory.make(UUID.randomUUID());

    var expectedParticipants = ParticipantFactory.makeMany(trip);
    when(repository.findByTripId(eq(trip.getId()))).thenReturn(expectedParticipants);

    var participants = this.service.getAllParticipantsFromTrip(trip);

    assertEquals(expectedParticipants.size(), participants.size());
    assertTrue(IntStream.range(0, expectedParticipants.size()).allMatch(i ->
        expectedParticipants.get(i).getName().equals(participants.get(i).name())
            && expectedParticipants.get(i).getEmail().equals(participants.get(i).email())));
  }

}
