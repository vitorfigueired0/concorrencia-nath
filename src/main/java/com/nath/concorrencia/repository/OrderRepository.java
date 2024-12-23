package com.nath.concorrencia.repository;

import com.nath.concorrencia.model.Order;
import org.springframework.data.repository.Repository;

@org.springframework.stereotype.Repository
public interface OrderRepository extends Repository<Order, Long> {
}
