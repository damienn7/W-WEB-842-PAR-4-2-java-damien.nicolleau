package com.cwa.crudspringboot.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cwa.crudspringboot.entity.Allergen;

public interface AllergenRepository extends JpaRepository<Allergen, Long> {
}
