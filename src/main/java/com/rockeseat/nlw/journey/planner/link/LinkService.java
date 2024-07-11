package com.rockeseat.nlw.journey.planner.link;

import com.rockeseat.nlw.journey.planner.trip.Trip;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class LinkService {

  @Autowired
  private LinkRepository repository;

  public LinkCreateResponse registerLink(LinkRequestPayload payload, Trip trip) {
    var link = new Link(payload.title(), payload.url(), trip);

    this.repository.save(link);

    return new LinkCreateResponse(link.getId());
  }

  public List<LinkData> getAllParticipantsFromTripId(UUID tripId) {
    return this.repository.findByTripId(tripId).stream().map(activity ->
        new LinkData(activity.getId(), activity.getTitle(), activity.getUrl())
    ).toList();
  }

}
