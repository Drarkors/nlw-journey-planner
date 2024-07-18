package com.rockeseat.nlw.journey.planner.participant.controller;

import com.rockeseat.nlw.journey.planner.participant.dtos.ParticipantRequestPayload;
import com.rockeseat.nlw.journey.planner.participant.factories.ParticipantFactory;
import com.rockeseat.nlw.journey.planner.participant.repository.ParticipantRepository;
import com.rockeseat.nlw.journey.planner.trip.factories.TripFactory;
import com.rockeseat.nlw.journey.planner.utils.MvcTestUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
public class ParticipantControllerTest {
  private static final String URL_PREFIX = "/participants";

  @Autowired
  private TripFactory tripFactory;

  @Autowired
  private ParticipantFactory participantFactory;

  @Autowired
  private ParticipantRepository repository;

  @Autowired
  private MockMvc mockMvc;

  @Test
  void registerActivity_shouldRegisterActivity() throws Exception {

    var trip = this.tripFactory.make();
    var participant = this.participantFactory.makeUnconfirmed(trip);
    var payload = new ParticipantRequestPayload("Participant Name", "participant@mail.com");

    this.mockMvc.perform(post(
            URL_PREFIX.concat("/")
                .concat(participant.getId().toString())
                .concat("/confirm"))
            .content(MvcTestUtils.objectToJson(payload))
            .contentType(MediaType.APPLICATION_JSON))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(content().string(containsString("\"name\":\"Participant Name\"")))
        .andExpect(content().string(containsString("\"email\":\"participant@mail.com\"")))
        .andExpect(content().string(containsString("\"isConfirmed\":true")));
  }

  @Test
  void registerActivity_shouldThrowParticipantNotFoundExceptionOnInvalidId() throws Exception {
    var payload = new ParticipantRequestPayload("Participant Name", "participant@mail.com");

    this.mockMvc.perform(post(
            URL_PREFIX.concat("/")
                .concat(UUID.randomUUID().toString())
                .concat("/confirm"))
            .content(MvcTestUtils.objectToJson(payload))
            .contentType(MediaType.APPLICATION_JSON))
        .andDo(print())
        .andExpect(status().isNotFound());
  }

}
