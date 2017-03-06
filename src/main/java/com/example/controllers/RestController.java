package com.example.controllers;

import com.example.models.Car;
import com.example.models.User;
import com.example.repository.CarRepository;
import com.example.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * Created by PatrykGrudnik on 13.12.2016.
 */

@org.springframework.web.bind.annotation.RestController
public class RestController {


    public final CarRepository carRepository;

    @Autowired
    public RestController(CarRepository carRepository) {
        this.carRepository = carRepository;
    }


    @RequestMapping("ca1rs")
    public List<Car> main(String[] args) {
        List<Car> carList = carRepository.findAll();
        return carList;
    }

}
