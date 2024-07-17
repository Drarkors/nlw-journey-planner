package com.rockeseat.nlw.journey.planner.trip.controllers;

import com.rockeseat.nlw.journey.planner.link.dtos.LinkData;
import com.rockeseat.nlw.journey.planner.link.factories.LinkRequestPayloadFactory;
import com.rockeseat.nlw.journey.planner.link.repository.LinkRepository;
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
public class TripLinksControllerTest {

  private static final String URL_PREFIX = "/trips";

  @Autowired
  private TripFactory tripFactory;

  @Autowired
  private LinkRepository linkRepository;

  @Autowired
  private MockMvc mockMvc;

  @Test
  @Order(0)
  void registerLink_shouldRegisterALink() throws Exception {
    var trip = this.tripFactory.make();

    var payload = LinkRequestPayloadFactory.fake();

    this.mockMvc.perform(post(
            URL_PREFIX.concat("/")
                .concat(trip.getId().toString())
                .concat("/links"))
            .content(MvcTestUtils.objectToJson(payload))
            .contentType(MediaType.APPLICATION_JSON))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(content().string(containsString("linkId")));
  }

  @Test
  @Order(1)
  void registerLink_shouldReturnTripNotFoundExceptionOnInvalidTripId() throws Exception {
    var payload = LinkRequestPayloadFactory.fake();

    this.mockMvc.perform(get(
            URL_PREFIX.concat("/")
                .concat(UUID.randomUUID().toString())
                .concat("/links"))
            .content(MvcTestUtils.objectToJson(payload))
            .contentType(MediaType.APPLICATION_JSON))
        .andDo(print())
        .andExpect(status().isNotFound());
  }


  @Test
  @Order(1)
  void getAllLinks_shouldReturnListOfLinksFromTrip() throws Exception {
    var links = this.linkRepository.findAll();
    var trip = links.get(0).getTrip();

    var expectedResponse = links.stream().map(link ->
        new LinkData(link.getId(), link.getTitle(), link.getUrl())).toList();

    this.mockMvc.perform(get(
            URL_PREFIX.concat("/")
                .concat(trip.getId().toString())
                .concat("/links")))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(content().json(MvcTestUtils.objectToJson(expectedResponse)));
  }
}
