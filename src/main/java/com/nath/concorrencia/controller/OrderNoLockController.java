package com.nath.concorrencia.controller;

import com.nath.concorrencia.enums.OrderLockType;
import com.nath.concorrencia.model.dto.OrderDTO;
import com.nath.concorrencia.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/pedido")
public class OrderNoLockController {

  @Autowired
  private OrderService orderService;

  @PostMapping("/novo")
  @ResponseStatus(HttpStatus.CREATED)
  private CompletableFuture<OrderDTO> createOrder(@RequestBody OrderDTO dto) {
    return orderService.createOrder(dto, OrderLockType.NO_LOCK);
  }

}
