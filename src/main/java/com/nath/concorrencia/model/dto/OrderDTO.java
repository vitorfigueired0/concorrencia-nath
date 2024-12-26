package com.nath.concorrencia.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class OrderDTO {
  private Long id;
  private Long clientId;
  private LocalDateTime orderDate;
  private List<ProductDTO> products;
}
