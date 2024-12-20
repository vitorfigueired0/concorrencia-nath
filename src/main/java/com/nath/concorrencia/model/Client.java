package com.nath.concorrencia.model;

import jakarta.persistence.*;

@Entity
public class Client {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String name;
  private String role;
  private String address;
  private String city;
  private String postalCode;
  


}
