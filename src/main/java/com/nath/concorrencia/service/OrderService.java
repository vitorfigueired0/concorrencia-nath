package com.nath.concorrencia.service;

import com.nath.concorrencia.enums.OrderLockType;
import com.nath.concorrencia.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderService {

  @Autowired
  private OrderRepository orderRepository;
  @Autowired
  private ProductService productService;

  public void createOrder(OrderLockType type) {

    switch (type) {
      case NO_LOCK:
        System.out.println("no lock");
        break;

      case OPTIMISTIC:
        System.out.println("Optimistic");
        break;

      case PESSIMISTIC:
        System.out.println("Pessimistic");
        break;
    }



  }

}
