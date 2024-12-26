package com.nath.concorrencia.service;

import com.nath.concorrencia.enums.OrderLockType;
import com.nath.concorrencia.model.OrderDetail;
import com.nath.concorrencia.model.Product;
import com.nath.concorrencia.model.dto.OrderDTO;
import com.nath.concorrencia.model.dto.ProductDTO;
import com.nath.concorrencia.repository.OrderRepository;
import com.nath.concorrencia.repository.ProductRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import java.util.ArrayList;
import java.util.List;

@Service
public class OrderService {

  @Autowired
  private OrderRepository orderRepository;

  @Autowired
  private ProductRepository productRepository;

  public void createOrder(OrderDTO orderDTO, OrderLockType type) {

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


  @Transactional //Essa anotação faz com que o tópico "Tanto o salvamento quanto o débito de estoque devem ser operações atômicas dentro da mesma transação." seja atendida
  private void createOrderWithoutLock(OrderDTO orderDTO) {
    List<ProductDTO> products = orderDTO.getProducts();
    List<OrderDetail> details = new ArrayList<>();

    for(ProductDTO productDto : products) {
      Product product = productRepository.findById(productDto.getId())
          .orElseThrow(() -> new HttpClientErrorException(HttpStatusCode.valueOf(404), "Product not found"));

      if(product.getInStockQuantity() < productDto.getQuantity()) {
        throw new HttpClientErrorException(HttpStatusCode.valueOf(409), "Out of stock");
      }

      OrderDetail detail = OrderDetail.builder().build();
    }






  }

}
