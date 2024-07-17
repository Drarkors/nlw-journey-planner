package com.rockeseat.nlw.journey.planner.link.factories;

import com.rockeseat.nlw.journey.planner.link.dtos.LinkRequestPayload;

public class LinkRequestPayloadFactory {

  public static LinkRequestPayload fake() {
    return new LinkRequestPayload("Title", "url://site.com");
  }

}
