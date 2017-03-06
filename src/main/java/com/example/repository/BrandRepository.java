package com.example.repository;

import com.example.models.Brand;
import com.example.models.Car;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by PatrykGrudnik on 13.12.2016.
 */
@Repository
public interface BrandRepository extends JpaRepository<Brand, Integer> {
}
