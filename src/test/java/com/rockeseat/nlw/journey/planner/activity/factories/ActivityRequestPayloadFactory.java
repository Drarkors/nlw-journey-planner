package com.rockeseat.nlw.journey.planner.activity.factories;

import com.rockeseat.nlw.journey.planner.activity.dtos.ActivityRequestPayload;

public class ActivityRequestPayloadFactory {
  public static ActivityRequestPayload make(String occursAt) {
    return new ActivityRequestPayload("Title", occursAt);
  }
}
