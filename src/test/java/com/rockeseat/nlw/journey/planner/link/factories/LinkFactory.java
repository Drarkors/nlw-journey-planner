package com.rockeseat.nlw.journey.planner.link.factories;

import com.rockeseat.nlw.journey.planner.link.Link;
import com.rockeseat.nlw.journey.planner.trip.Trip;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class LinkFactory {

  public static Link make(Trip trip) {
    return new Link(UUID.randomUUID(), "Title", "url://site.com", trip);
  }

  public static List<Link> makeMany(Trip trip) {
    var links = new ArrayList<Link>();
    links.add(new Link(UUID.randomUUID(), "Title 1", "url://1.com", trip));
    links.add(new Link(UUID.randomUUID(), "Title 2", "url://2.com", trip));

    return links;
  }

}
