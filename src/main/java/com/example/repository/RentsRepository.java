package com.example.repository;

import com.example.models.Rent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by PatrykGrudnik on 13.12.2016.
 */
@Repository
public interface RentsRepository extends JpaRepository<Rent, Integer> {
    List<Rent> findByUser_id(int id);
    List<Rent> findByCar_id(int id);


    Rent findById(int id);
}




