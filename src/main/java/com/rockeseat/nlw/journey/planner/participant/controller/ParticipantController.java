package com.rockeseat.nlw.journey.planner.participant.controller;

import com.rockeseat.nlw.journey.planner.participant.dtos.ParticipantRequestPayload;
import com.rockeseat.nlw.journey.planner.participant.service.ParticipantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/participants")
public class ParticipantController {

  @Autowired
  private ParticipantService participantService;


  @PostMapping("/{id}/confirm")
  public ResponseEntity<?> confirmParticipant(@PathVariable UUID id, @RequestBody ParticipantRequestPayload payload) {
    return ResponseEntity.ok(this.participantService.confirmParticipant(id, payload));
  }

}
