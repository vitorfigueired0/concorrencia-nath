package com.nath.concorrencia.model;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.server.ResponseStatusException;

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

  public void verifyStock(Integer requestedQuantity) {
    if(this.inStockQuantity < requestedQuantity) {
      throw new ResponseStatusException(HttpStatusCode.valueOf(409), "Out of stock");
    }
  }
}
