package com.link.OnlineShop.database;

import org.aspectj.weaver.ast.Or;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderDao  extends JpaRepository<Order, Integer> {
    List<Order> findAllByUserId(int id);
}
