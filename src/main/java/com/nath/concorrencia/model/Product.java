package com.nath.concorrencia.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@With
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "products")
public class Product {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(length = 30)
  private String name;

  @ManyToOne
  @JoinColumn(name = "category_id", nullable = false)
  private Category category;

  @Column(columnDefinition = "DOUBLE(10,2)")
  private Double price;

  @Column(name = "stock")
  private Integer inStockQuantity;

  @Version
  private Integer version; //<-- esse atributo controla a lock otimista

}
