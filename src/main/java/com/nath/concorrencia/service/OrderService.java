package com.nath.concorrencia.service;

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
import jakarta.persistence.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

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

  @Autowired
  private EntityManagerFactory emf;
  private final ObjectMapper mapper = new ObjectMapper();
  private final String INSERT_INTO_ORDERS = "INSERT INTO orders (client_id, order_date) VALUES (:clientId, :orderDate)";
  private final String SELECT_FROM_ORDERS = "SELECT id, client_id, order_date FROM orders WHERE id = LAST_INSERT_ID()";
  private final String INSERT_INTO_ORDERDETAILS = """
      INSERT INTO order_details (order_id, sell_price, quantity, product_id) VALUES
      (:orderId, :sellPrice, :quantity, :productId)
    """;


  @Async
  public CompletableFuture<OrderDTO> createOrder(OrderDTO orderDTO, OrderLockType type) {
    return CompletableFuture.supplyAsync(() -> {
      Client client = clientService.getById(orderDTO.getClientId());
      Order order = Order.builder()
          .orderDate(LocalDateTime.now(ZoneId.of("America/Sao_Paulo")))
          .client(client)
          .build();

      Order resultOrder;

      switch (type) {
        case NO_LOCK -> resultOrder = createOrderWithoutLock(order, orderDTO.getProducts());
        case OPTIMISTIC -> resultOrder = createOrderWithOptimisticLock(order, orderDTO.getProducts());
        case PESSIMISTIC -> resultOrder = createOrderWithPessimisticLock(order, orderDTO.getProducts());
        default -> throw new IllegalArgumentException("Invalid lock type");
      }

      return orderDTO.buildDto(resultOrder);
    });
  }

  private Order createOrderWithoutLock(Order order, List<ProductDTO> products) {
    List<OrderDetail> details = new ArrayList<>();
    EntityManager entityManager = emf.createEntityManager();

    entityManager.getTransaction().begin();
    for(ProductDTO productDto : products) {
      Product product = entityManager.find(Product.class, productDto.getId());
      handleProduct(productDto, product);

      OrderDetail detail = OrderDetail.build(product, productDto);
      details.add(detail);

      updateProductStock(productDto, entityManager, product);
    }

    Order finalOrder = getFinalOrder(order, entityManager);

    saveDetails(details, entityManager, finalOrder);
    entityManager.getTransaction().commit();
    return finalOrder;
  }

  private Order createOrderWithPessimisticLock(Order order, List<ProductDTO> products) {
    List<OrderDetail> details = new ArrayList<>();
    EntityManager entityManager = emf.createEntityManager();

    entityManager.getTransaction().begin();
    for(ProductDTO productDto : products) {
      Product product = entityManager.find(Product.class, productDto.getId(), LockModeType.PESSIMISTIC_WRITE);
      handleProduct(productDto, product);

      OrderDetail detail = OrderDetail.build(product, productDto);
      details.add(detail);

      updateProductStock(productDto, entityManager, product);
    }

    Order finalOrder = getFinalOrder(order, entityManager);

    saveDetails(details, entityManager, finalOrder);
    entityManager.getTransaction().commit();
    return finalOrder;
  }

  private Order createOrderWithOptimisticLock(Order order, List<ProductDTO> products) {
    List<OrderDetail> details = new ArrayList<>();

    for(ProductDTO productDto : products) {
      Product product = productRepository.findById(productDto.getId()).orElseThrow(() -> new ResponseStatusException(HttpStatusCode.valueOf(404), "Product not found"));

      handleProduct(productDto, product);
      OrderDetail detail = OrderDetail.build(product, productDto);
      details.add(detail);

      try {
        productRepository.save(product.withInStockQuantity(product.getInStockQuantity() - productDto.getQuantity()));
      } catch (OptimisticLockException e) {
        throw new ResponseStatusException(HttpStatusCode.valueOf(500), "Product already updated");
      }
    }

    Order finalOrder = orderRepository.save(order);
    details.forEach(orderDetail -> orderDetailRepository.save(orderDetail.withOrder(finalOrder)));

    return finalOrder;
  }

  private void handleProduct(ProductDTO productDto, Product product) {
    product.verifyStock(productDto.getQuantity());
    productDto.setName(product.getName());
    productDto.setPrice(product.getPrice());
  }

  private void updateProductStock(ProductDTO productDto, EntityManager entityManager, Product product) {
    Query updateStock = entityManager.createNativeQuery("UPDATE products SET stock = :newStock WHERE id = :productId");
    updateStock.setParameter("newStock",(product.getInStockQuantity() - productDto.getQuantity()));
    updateStock.setParameter("productId", product.getId());
    updateStock.executeUpdate();
  }

  private Order getFinalOrder(Order order, EntityManager entityManager) {
    Query saveOrder = entityManager.createNativeQuery(INSERT_INTO_ORDERS);
    saveOrder.setParameter("clientId", order.getClient().getId());
    saveOrder.setParameter("orderDate", order.getOrderDate());
    saveOrder.executeUpdate();

    Query fetchOrder = entityManager.createNativeQuery(SELECT_FROM_ORDERS, Order.class);
    return (Order) fetchOrder.getSingleResult();
  }


  private void saveDetails(List<OrderDetail> details, EntityManager entityManager, Order finalOrder) {
    details.forEach(orderDetail -> {
      Query saveDetails = entityManager.createNativeQuery(INSERT_INTO_ORDERDETAILS);
      saveDetails.setParameter("orderId", finalOrder.getId());
      saveDetails.setParameter("sellPrice", orderDetail.getSellPrice());
      saveDetails.setParameter("quantity", orderDetail.getQuantity());
      saveDetails.setParameter("productId", orderDetail.getProduct().getId());
    });
  }
}
