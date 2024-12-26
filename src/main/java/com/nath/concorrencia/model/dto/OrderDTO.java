package com.nath.concorrencia.model.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@With
@Getter
@Setter
public class OrderDTO {
  private Long id;
  private Long clientId;
  private LocalDateTime orderDate;
  private List<ProductDTO> products;
}
