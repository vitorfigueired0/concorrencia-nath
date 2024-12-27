package com.nath.concorrencia.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import jakarta.persistence.OptimisticLockException;
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

  private final ObjectMapper mapper = new ObjectMapper();

  public OrderDTO createOrder(OrderDTO orderDTO, OrderLockType type) {
    Client client = clientService.getById(orderDTO.getClientId());
    Order order = Order.builder()
        .orderDate(LocalDateTime.now(ZoneId.of("America/Sao_Paulo")))
        .client(client)
        .build();

    Order resultOrder;
    return switch (type) {
      case NO_LOCK -> {
        resultOrder = createOrderWithoutLock(order, orderDTO.getProducts());
        yield orderDTO.buildDto(resultOrder);
      }
      case OPTIMISTIC -> {
        resultOrder = createOrderWithOptimisticLock(order, orderDTO.getProducts());
        yield orderDTO.buildDto(resultOrder);
      }
      case PESSIMISTIC -> {
        resultOrder = createOrderWithPessimisticLock(order, orderDTO.getProducts());
        yield orderDTO.buildDto(resultOrder);
      }
    };
  }

  @Transactional //Essa anotação faz com que o tópico "Tanto o salvamento quanto o débito de estoque devem ser operações atômicas dentro da mesma transação." seja atendida
  private Order createOrderWithoutLock(Order order, List<ProductDTO> products) {
    List<OrderDetail> details = new ArrayList<>();

    for(ProductDTO productDto : products) {
      Product product = productRepository.findById(productDto.getId())
          .orElseThrow(() -> new HttpClientErrorException(HttpStatusCode.valueOf(404), "Product not found"));

      product.verifyStock(productDto.getQuantity());
      productDto.setName(product.getName());
      productDto.setPrice(product.getPrice());

      OrderDetail detail = OrderDetail.build(product, productDto);
      details.add(detail);
      productRepository.updateStockWithoutVersionCheck(product.getId(), (product.getInStockQuantity() - productDto.getQuantity()));
    }

    Order finalOrder = orderRepository.save(order);
    details.forEach(orderDetail -> orderDetailRepository.save(orderDetail.withOrder(finalOrder)));

    return finalOrder;
  }

  @Transactional //Essa anotação faz com que o tópico "Tanto o salvamento quanto o débito de estoque devem ser operações atômicas dentro da mesma transação." seja atendida
  private Order createOrderWithOptimisticLock(Order order, List<ProductDTO> products) {
    List<OrderDetail> details = new ArrayList<>();

    for(ProductDTO productDto : products) {
      Product product = productRepository.findById(productDto.getId())
          .orElseThrow(() -> new HttpClientErrorException(HttpStatusCode.valueOf(404), "Product not found"));

      product.verifyStock(productDto.getQuantity());
      productDto.setName(product.getName());
      productDto.setPrice(product.getPrice());
      OrderDetail detail = OrderDetail.build(product, productDto);
      details.add(detail);

      try {
        productRepository.save(product.withInStockQuantity(product.getInStockQuantity() - productDto.getQuantity()));
      } catch (OptimisticLockException e) {
        throw new HttpClientErrorException(HttpStatusCode.valueOf(500), "Product already updated");
      }
    }

    Order finalOrder = orderRepository.save(order);
    details.forEach(orderDetail -> orderDetailRepository.save(orderDetail.withOrder(finalOrder)));

    return finalOrder;
  }

  @Transactional //Essa anotação faz com que o tópico "Tanto o salvamento quanto o débito de estoque devem ser operações atômicas dentro da mesma transação." seja atendida
  private Order createOrderWithPessimisticLock(Order order, List<ProductDTO> products) {
    List<OrderDetail> details = new ArrayList<>();

    for(ProductDTO productDto : products) {
      Product product = productRepository.findByIdWithPessimisticLock(productDto.getId())
          .orElseThrow(() -> new HttpClientErrorException(HttpStatusCode.valueOf(404), "Product not found"));

      product.verifyStock(productDto.getQuantity());
      productDto.setName(product.getName());
      productDto.setPrice(product.getPrice());
      OrderDetail detail = OrderDetail.build(product, productDto);
      details.add(detail);
      productRepository.save(product.withInStockQuantity(product.getInStockQuantity() - productDto.getQuantity()));
    }

    Order finalOrder = orderRepository.save(order);
    details.forEach(orderDetail -> orderDetailRepository.save(orderDetail.withOrder(finalOrder)));
    return finalOrder;
  }

}
