package com.cwa.crudspringboot.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cwa.crudspringboot.entity.Dish;

public interface DishRepository extends JpaRepository<Dish, Long> {
}
