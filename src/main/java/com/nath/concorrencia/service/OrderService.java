package com.nath.concorrencia.service;

import com.nath.concorrencia.enums.OrderLockType;
import com.nath.concorrencia.model.Client;
import com.nath.concorrencia.model.Order;
import com.nath.concorrencia.model.OrderDetail;
import com.nath.concorrencia.model.Product;
import com.nath.concorrencia.model.dto.OrderDTO;
import com.nath.concorrencia.model.dto.ProductDTO;
import com.nath.concorrencia.repository.OrderDetailRepository;
import com.nath.concorrencia.repository.OrderRepository;
import com.nath.concorrencia.repository.ProductRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrderService {

  @Autowired
  private OrderRepository orderRepository;

  @Autowired
  private ProductRepository productRepository;

  @Autowired
  private OrderDetailRepository orderDetailRepository;

  @Autowired
  private ClientService clientService;

  public void createOrder(OrderDTO orderDTO, OrderLockType type) {
    switch (type) {
      case NO_LOCK:
        createOrderWithoutLock(orderDTO);
        break;

      case OPTIMISTIC:
        createOrderWithOptimisticLock(orderDTO);
        break;

      case PESSIMISTIC:
        createOrderWithPessimisticLock(orderDTO);
        break;
    }
  }


  @Transactional //Essa anotação faz com que o tópico "Tanto o salvamento quanto o débito de estoque devem ser operações atômicas dentro da mesma transação." seja atendida
  private void createOrderWithoutLock(OrderDTO orderDTO) {
    List<ProductDTO> products = orderDTO.getProducts();
    List<OrderDetail> details = new ArrayList<>();

    Client client = clientService.getById(orderDTO.getClientId());
    Order order = Order.builder()
        .orderDate(LocalDateTime.now(ZoneId.of("America/Sao_Paulo")))
        .client(client)
        .build();

    for(ProductDTO productDto : products) {
      Product product = productRepository.findById(productDto.getId())
          .orElseThrow(() -> new HttpClientErrorException(HttpStatusCode.valueOf(404), "Product not found"));

      if(product.getInStockQuantity() < productDto.getQuantity()) {
        throw new HttpClientErrorException(HttpStatusCode.valueOf(409), "Out of stock");
      }

      OrderDetail detail = OrderDetail.builder()
          .quantity(productDto.getQuantity())
          .sellPrice(product.getPrice() * productDto.getQuantity())
          .product(product)
          .build();

      details.add(detail);
      productRepository.updateStockWithoutVersionCheck(product.getId(), (product.getInStockQuantity() - productDto.getQuantity()));
    }

    Order finalOrder = orderRepository.save(order);
    details.forEach(orderDetail -> {
      orderDetailRepository.save(orderDetail.withOrder(finalOrder));
    });
  }

  @Transactional //Essa anotação faz com que o tópico "Tanto o salvamento quanto o débito de estoque devem ser operações atômicas dentro da mesma transação." seja atendida
  private void createOrderWithOptimisticLock(OrderDTO orderDTO) {
    List<ProductDTO> products = orderDTO.getProducts();
    List<OrderDetail> details = new ArrayList<>();

    Client client = clientService.getById(orderDTO.getClientId());
    Order order = Order.builder()
        .orderDate(LocalDateTime.now(ZoneId.of("America/Sao_Paulo")))
        .client(client)
        .build();

    for(ProductDTO productDto : products) {
      Product product = productRepository.findById(productDto.getId())
          .orElseThrow(() -> new HttpClientErrorException(HttpStatusCode.valueOf(404), "Product not found"));

      if(product.getInStockQuantity() < productDto.getQuantity()) {
        throw new HttpClientErrorException(HttpStatusCode.valueOf(409), "Out of stock");
      }

      OrderDetail detail = OrderDetail.builder()
          .quantity(productDto.getQuantity())
          .sellPrice(product.getPrice() * productDto.getQuantity())
          .product(product)
          .build();

      details.add(detail);
      productRepository.save(product.withInStockQuantity(product.getInStockQuantity() - productDto.getQuantity()));
    }

    Order finalOrder = orderRepository.save(order);
    details.forEach(orderDetail -> {
      orderDetailRepository.save(orderDetail.withOrder(finalOrder));
    });
  }

  @Transactional //Essa anotação faz com que o tópico "Tanto o salvamento quanto o débito de estoque devem ser operações atômicas dentro da mesma transação." seja atendida
  private void createOrderWithPessimisticLock(OrderDTO orderDTO) {
    List<ProductDTO> products = orderDTO.getProducts();
    List<OrderDetail> details = new ArrayList<>();

    Client client = clientService.getById(orderDTO.getClientId());
    Order order = Order.builder()
        .orderDate(LocalDateTime.now(ZoneId.of("America/Sao_Paulo")))
        .client(client)
        .build();

    for(ProductDTO productDto : products) {
      Product product = productRepository.findById(productDto.getId())
          .orElseThrow(() -> new HttpClientErrorException(HttpStatusCode.valueOf(404), "Product not found"));

      if(product.getInStockQuantity() < productDto.getQuantity()) {
        throw new HttpClientErrorException(HttpStatusCode.valueOf(409), "Out of stock");
      }

      OrderDetail detail = OrderDetail.builder()
          .quantity(productDto.getQuantity())
          .sellPrice(product.getPrice() * productDto.getQuantity())
          .product(product)
          .build();

      details.add(detail);
      productRepository.updateStockWithoutVersionCheck(product.getId(), (product.getInStockQuantity() - productDto.getQuantity()));
    }

    Order finalOrder = orderRepository.save(order);
    details.forEach(orderDetail -> {
      orderDetailRepository.save(orderDetail.withOrder(finalOrder));
    });
  }


}
