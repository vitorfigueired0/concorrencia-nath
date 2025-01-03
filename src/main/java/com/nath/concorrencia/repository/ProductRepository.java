package com.nath.concorrencia.repository;

import com.nath.concorrencia.model.Product;
import jakarta.persistence.LockModeType;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

  @Modifying
  @Query("UPDATE Product p SET p.inStockQuantity = :newStock WHERE p.id = :productId")
  void updateStockWithoutVersionCheck(@Param("productId") Long productId, @Param("newStock") int newStock);

  @Lock(LockModeType.PESSIMISTIC_WRITE)
  @Query("SELECT p FROM Product p WHERE p.id = :id")
  Optional<Product> findByIdWithPessimisticLock(@Param("id") Long id);

}
