package com.rockeseat.nlw.journey.planner.link.service;

import com.rockeseat.nlw.journey.planner.link.Link;
import com.rockeseat.nlw.journey.planner.link.factories.LinkFactory;
import com.rockeseat.nlw.journey.planner.link.factories.LinkRequestPayloadFactory;
import com.rockeseat.nlw.journey.planner.link.repository.LinkRepository;
import com.rockeseat.nlw.journey.planner.trip.factories.TripFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class LinkServiceTest {

  @Mock
  private LinkRepository linkRepository;

  @InjectMocks
  private LinkService service;

  @Test
  void registerLink_shouldCreateLink() {
    var trip = TripFactory.fake(UUID.randomUUID());
    var expectedId = UUID.randomUUID();
    var payload = LinkRequestPayloadFactory.fake();

    when(this.linkRepository.save(any(Link.class))).thenAnswer((invocation) ->
        new Link(expectedId, payload.title(), payload.url(), trip));

    var link = this.service.registerLink(payload, trip);

    assertEquals(expectedId, link.getId());
    assertEquals(payload.title(), link.getTitle());
    assertEquals(payload.url(), link.getUrl());
  }

  @Test
  void getAllParticipantsFromTripId_shouldReturnLinks() {
    var trip = TripFactory.fake(UUID.randomUUID());
    var expectedLinks = LinkFactory.fakeMany(trip);

    when(this.linkRepository.findByTripId(eq(trip.getId()))).thenReturn(expectedLinks);

    var links = this.service.getAllParticipantsFromTripId(trip.getId());

    assertTrue(IntStream.range(0, expectedLinks.size()).allMatch(i ->
        expectedLinks.get(i).getTitle().equals(links.get(i).title())
            && expectedLinks.get(i).getUrl().equals(links.get(i).url())));
  }

}
