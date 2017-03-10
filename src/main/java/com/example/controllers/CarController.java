package com.example.controllers;

import com.example.models.Brand;
import com.example.models.Car;
import com.example.models.Rent;
import com.example.models.User;
import com.example.repository.BrandRepository;
import com.example.repository.CarRepository;
import com.example.repository.RentsRepository;
import com.example.repository.UserRepository;
import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.springframework.security.core.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.List;

/**
 * Created by PatrykGrudnik on 13.12.2016.
 */
@Controller
@RequestMapping(value = "/cars")
public class CarController {

    public final CarRepository carRepository;
    public final BrandRepository brandRepository;
    public final RentsRepository rentsRepository;
    public final UserRepository userRepository;


    @Autowired
    public CarController(CarRepository carRepository, BrandRepository brandRepository, RentsRepository rentsRepository, UserRepository userRepository) {
        this.carRepository = carRepository;
        this.brandRepository = brandRepository;
        this.rentsRepository = rentsRepository;
        this.userRepository = userRepository;
    }

    @RequestMapping(method = RequestMethod.GET)
    public String show(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        User user = userRepository.findByMail(auth.getName());
        List<Car> carsList = carRepository.findAll();
        List<Brand> brandsList = brandRepository.findAll();
        model.addAttribute("cars", carsList);
        model.addAttribute("userName", user.getName());
        model.addAttribute("brands", brandsList);
        return "cars";
    }

    @RequestMapping(value = "add", method = RequestMethod.POST)
    public String add(@Valid @ModelAttribute("car") Car car,
                      BindingResult bindingResult,
                      HttpServletRequest request,
                      RedirectAttributes redirectAttrs) {
        if (!bindingResult.hasErrors()) {

            carRepository.save(car);
            redirectAttrs.addFlashAttribute("message", "Added");
        } else {
            redirectAttrs.addFlashAttribute("message", "Not added");
        }
        String referer = request.getHeader("Referer");
        return "redirect:" + referer;
    }

    @RequestMapping(value = "/delete/{car_id}", method = RequestMethod.GET)
    public String delete(@PathVariable int car_id, HttpServletRequest request, RedirectAttributes redirectAttrs){

        Car car  = carRepository.findOne(car_id);
        if(car.isAvailable()){
            try {
                carRepository.delete(car);

            }catch (Exception e){
                redirectAttrs.addFlashAttribute("message", "Not deleted : "+e.getMessage());
            }


        }else {

            redirectAttrs.addFlashAttribute("message", "Not deleted : status");
        }



        String referer = request.getHeader("Referer");
        return "redirect:" + referer;
    }

    @RequestMapping(value = "/more/{car_id}", method = RequestMethod.GET)
    private String more (@PathVariable int car_id,Model model, HttpServletRequest request,  RedirectAttributes redirectAttrs){

        Car car = carRepository.findOne(car_id);
        List<Rent> rents = rentsRepository.findByCar_id(car_id);
        model.addAttribute("cars", car);
        model.addAttribute("rents", rents);

        return "car";
    }
}
