package com.tobeto.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.tobeto.entity.Box;

public interface BoxRepository extends JpaRepository<Box, Integer> {
	@Query("SELECT b FROM Box b WHERE b.fruit.id = :fruitId and b.count < b.capacity")
	Optional<Box> findByFruitIdNotFull(int fruitId);

	List<Box> findAllByCount(int count);

	List<Box> findAllByFruitIdAndCountGreaterThan(int id, int count);

	@Query("SELECT sum(b.count) FROM Box b WHERE b.fruit.id = :fruitId")
	Integer getFruitCount(int fruitId);
}
