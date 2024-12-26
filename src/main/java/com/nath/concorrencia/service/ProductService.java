package com.nath.concorrencia.service;

import com.nath.concorrencia.model.Product;
import com.nath.concorrencia.repository.ProductRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import java.util.Optional;

@Service
public class ProductService {

  @Autowired
  private ProductRepository productRepository;

  @Transactional //Essa anotação faz com que o tópico "Tanto o salvamento quanto o débito de estoque devem ser operações atômicas dentro da mesma transação." seja atendida
  private void updateProductStockWithoutLock(Long productId, int orderQuantity) {
    Product product = productRepository.findById(productId)
      .orElseThrow(() -> new HttpClientErrorException(HttpStatusCode.valueOf(404), "Product not found"));

    if(product.getInStockQuantity() < orderQuantity) {
      throw new HttpClientErrorException(HttpStatusCode.valueOf(409), "Out of stock");
    }




  }
}
