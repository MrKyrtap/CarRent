package com.example.controllers;

import com.example.models.Brand;
import com.example.models.Car;
import com.example.models.Rent;
import com.example.models.User;
import com.example.repository.CarRepository;
import com.example.repository.RentsRepository;
import com.example.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

/**
 * Created by PatrykGrudnik on 05.02.2017.
 */
@Controller
@RequestMapping(value = "/rent")
public class RentController {
    public final UserRepository userRepository;
    public final RentsRepository rentsRepository;
    public final CarRepository carRepository;

    @Autowired
    public RentController(UserRepository userRepository, RentsRepository rentsRepository, CarRepository carRepository) {
        this.userRepository = userRepository;
        this.rentsRepository = rentsRepository;
        this.carRepository = carRepository;
    }

    @RequestMapping(value = "/end/{foo}", method = RequestMethod.GET)
    public String end(@PathVariable String foo, HttpServletRequest request) {
        Rent rent = rentsRepository.findOne(Integer.parseInt(foo));
        Car car;
        rent.setActive(false);
        rent.setDate_to(new Date());
        car = rent.getCar();
        car.setAvailable(true);
        rentsRepository.save(rent);
        carRepository.save(car);

        String referer = request.getHeader("Referer");

        return "redirect:" + referer;
    }


    @RequestMapping(value = "/new/{foo}", method = RequestMethod.GET)
    public String newrent(@PathVariable String foo, Model model) {
        User user = userRepository.findOne(Integer.parseInt(foo));
        List<Car> carstorent = carRepository.findByAvailable(true);

        model.addAttribute("cars", carstorent);
        model.addAttribute("name", user);
        return "newrent";
    }

    @RequestMapping(value = "/new/{userid}/{carid}", method = RequestMethod.GET)
    public String newrent(@PathVariable String userid, @PathVariable String carid, Model model) {

        User user = userRepository.findOne(Integer.parseInt(userid));
        Car car = carRepository.findOne(Integer.parseInt(carid));
        model.addAttribute("cars", car);
        model.addAttribute("name", user);

        model.addAttribute("finish", true);

        return "newrent";


    }

    @RequestMapping(value = "/new/{userid}/{carid}/agreement", method = RequestMethod.GET)
    public String agreement(@PathVariable String userid, @PathVariable String carid, Model model) {

        User user = userRepository.findOne(Integer.parseInt(userid));
        Car car = carRepository.findOne(Integer.parseInt(carid));
        model.addAttribute("cars", car);
        model.addAttribute("name", user);
        return "agreement";
    }

    @RequestMapping(value = "/new/{userid}/{carid}/add", method = RequestMethod.GET)
    public String add(@PathVariable String userid, @PathVariable String carid, Model model) {

        User user = userRepository.findOne(Integer.parseInt(userid));
        Car car = carRepository.findOne(Integer.parseInt(carid));
        List<Rent> rents;
        if (car.isAvailable()) {
            Rent rent = new Rent();
            rent.setUser(user);
            rent.setCar(car);
            rent.setActive(true);
            rent.setDate_from(new Date());
            car.setAvailable(false);
            carRepository.save(car);
            rentsRepository.save(rent);
        }

        rents = rentsRepository.findByUser_id(Integer.parseInt(userid));
        model.addAttribute("rents", rents);
        model.addAttribute("name", user);
        return "user";
    }


}
