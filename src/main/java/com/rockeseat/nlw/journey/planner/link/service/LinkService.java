package com.rockeseat.nlw.journey.planner.link.service;

import com.rockeseat.nlw.journey.planner.link.Link;
import com.rockeseat.nlw.journey.planner.link.dtos.LinkData;
import com.rockeseat.nlw.journey.planner.link.dtos.LinkRequestPayload;
import com.rockeseat.nlw.journey.planner.link.repository.LinkRepository;
import com.rockeseat.nlw.journey.planner.trip.Trip;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class LinkService {

  @Autowired
  private LinkRepository repository;

  public Link registerLink(LinkRequestPayload payload, Trip trip) {
    var link = new Link(payload.title(), payload.url(), trip);
    
    return this.repository.save(link);
  }

  public List<LinkData> getAllParticipantsFromTripId(UUID tripId) {
    return this.repository.findByTripId(tripId).stream().map(activity ->
        new LinkData(activity.getId(), activity.getTitle(), activity.getUrl())
    ).toList();
  }

}
