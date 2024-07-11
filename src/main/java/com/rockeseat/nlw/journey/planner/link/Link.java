package com.rockeseat.nlw.journey.planner.link;

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
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "links")
public class Link {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private UUID id;

  @Column
  private String title;

  @Column
  private String url;

  @ManyToOne
  @JoinColumn(name = "trip_id", nullable = false)
  private Trip trip;

  public Link(String title, String url, Trip trip) {
    this.title = title;
    this.url = url;
    this.trip = trip;
  }
}
