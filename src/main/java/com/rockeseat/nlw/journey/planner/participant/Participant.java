package com.rockeseat.nlw.journey.planner.participant;

import com.rockeseat.nlw.journey.planner.trip.Trip;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "participants")
public class Participant {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private UUID id;

  @Column(name ="is_confirmed", nullable = false)
  private Boolean isConfirmed;

  @Column(nullable = false)
  private String name;

  @Column(nullable = false)
  private String email;

  @ManyToOne
  @JoinColumn(name ="trip_id", nullable = false)
  private Trip trip;

  public Participant(String email, Trip trip) {
    this.email = email;
    this.trip = trip;

    this.name = "";
    this.isConfirmed = false;
  }

}
