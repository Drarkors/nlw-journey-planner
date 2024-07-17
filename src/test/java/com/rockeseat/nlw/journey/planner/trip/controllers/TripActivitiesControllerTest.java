package com.rockeseat.nlw.journey.planner.trip.controllers;

import com.rockeseat.nlw.journey.planner.activity.dtos.ActivityData;
import com.rockeseat.nlw.journey.planner.activity.factories.ActivityRequestPayloadFactory;
import com.rockeseat.nlw.journey.planner.activity.repository.ActivityRepository;
import com.rockeseat.nlw.journey.planner.trip.factories.TripFactory;
import com.rockeseat.nlw.journey.planner.utils.MvcTestUtils;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest
@AutoConfigureMockMvc
public class TripActivitiesControllerTest {
  private static final String URL_PREFIX = "/trips";

  @Autowired
  private TripFactory tripFactory;

  @Autowired
  private ActivityRepository activityRepository;

  @Autowired
  private MockMvc mockMvc;

  @Test
  @Order(0)
  void registerActivity_shouldRegisterActivity() throws Exception {
    var trip = this.tripFactory.make();

    var payload = ActivityRequestPayloadFactory.fake(LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME));

    this.mockMvc.perform(post(
            URL_PREFIX.concat("/")
                .concat(trip.getId().toString())
                .concat("/activities"))
            .content(MvcTestUtils.objectToJson(payload))
            .contentType(MediaType.APPLICATION_JSON))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(content().string(containsString("activityId")));
  }

  @Test
  @Order(1)
  void registerActivity_shouldReturnTripNotFoundExceptionOnInvalidTripId() throws Exception {
    var payload = ActivityRequestPayloadFactory.fake(LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME));

    this.mockMvc.perform(get(
            URL_PREFIX.concat("/")
                .concat(UUID.randomUUID().toString())
                .concat("/activities"))
            .content(MvcTestUtils.objectToJson(payload))
            .contentType(MediaType.APPLICATION_JSON))
        .andDo(print())
        .andExpect(status().isNotFound());
  }

  @Test
  @Order(1)
  void getAllActivities_shouldReturnActivitiesFromTrip() throws Exception {
    var activities = this.activityRepository.findAll();
    var trip = activities.get(0).getTrip();

    var expectedResponse = activities.stream().map(activity ->
        new ActivityData(activity.getId(), activity.getTitle(),
            activity.getOccursAt().format(DateTimeFormatter.ISO_DATE_TIME))).toList();
    
    this.mockMvc.perform(get(
            URL_PREFIX.concat("/")
                .concat(trip.getId().toString())
                .concat("/activities")))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(content().json(MvcTestUtils.objectToJson(expectedResponse)));
  }
}
