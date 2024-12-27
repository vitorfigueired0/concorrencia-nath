package com.nath.concorrencia.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "clients")
public class Client {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(length = 30)
  private String name;

  @Column(length = 30)
  private String role;

  @Column(length = 60)
  private String address;

  @Column(length = 15)
  private String city;

  @Column(length = 10)
  private String postalCode;

  @Column(length = 24)
  private String phone;
}
