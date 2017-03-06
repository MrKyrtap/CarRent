package com.example.repository;

import com.example.models.Car;
import com.example.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by PatrykGrudnik on 13.12.2016.
 */
@Repository
public interface CarRepository extends JpaRepository<Car, Integer> {
    List<Car> findByAvailable(Boolean bool);
}




