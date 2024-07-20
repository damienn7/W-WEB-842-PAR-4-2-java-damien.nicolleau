package com.cwa.crudspringboot.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cwa.crudspringboot.entity.Card;

public interface CardRepository extends JpaRepository<Card, Long> {
}
