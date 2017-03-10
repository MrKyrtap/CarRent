package com.example.controllers;

import com.example.models.User;
import com.example.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.util.List;

/**
 * Created by PatrykGrudnik on 05.12.2016.
 */
@Controller
public class IndexController {


    public final UserRepository userRepository;

    @Autowired
    public IndexController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/index")
    public String index(Model model) {
        List<User> usersList = userRepository.findAll(new Sort(Sort.Direction.ASC,"lastName"));
        model.addAttribute("name", usersList);

        return "index";
    }

    @GetMapping("/index/search")
    public String indexs(Model model) {
        List<User> usersList = userRepository.findAll();
        model.addAttribute("name", usersList);

        return "index";
    }

    @GetMapping("/index/search/{lastName}")
    public String indexsearch(@PathVariable String lastName, Model model) {
        List<User> usersList = userRepository.findByLastName(lastName);
        if (usersList.size() < 1)
            usersList = userRepository.findAll();

        model.addAttribute("name", usersList);

        return "index";
    }


    @GetMapping("/")
    public String tezIndex(Model model) {
        List<User> usersList = userRepository.findAll();
        model.addAttribute("name", usersList);

        return "index";
    }
}