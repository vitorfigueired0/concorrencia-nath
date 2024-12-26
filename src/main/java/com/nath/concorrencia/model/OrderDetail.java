package com.nath.concorrencia.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@With
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "order_details")
public class OrderDetail {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne
  @JoinColumn(name = "product_id", nullable = false)
  private Product product;

  @ManyToOne
  @JoinColumn(name = "order_id", nullable = false)
  private Order order;

  @Column(name = "sell_price", columnDefinition = "DOUBLE(10,2)")
  private Double sellPrice;
  private Integer quantity;

}
