package com.nath.concorrencia.model.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@With
@Getter
@Setter
public class ProductDTO {
  private Long id;
  private String name;
  private Double price;
  private Integer quantity;

}
