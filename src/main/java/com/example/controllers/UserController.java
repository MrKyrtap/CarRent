package com.example.controllers;

import com.example.models.Rent;
import com.example.models.Role;
import com.example.models.User;
import com.example.repository.RentsRepository;
import com.example.repository.RoleRepository;
import com.example.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

/**
 * Created by PatrykGrudnik on 05.12.2016.
 */
@Controller
@RequestMapping(value = "/user")
public class UserController {

    public final UserRepository userRepository;
    public final RentsRepository rentsRepository;
    public final RoleRepository roleRepository;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;


    @Autowired
    public UserController(UserRepository userRepository, RentsRepository rentsRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.rentsRepository = rentsRepository;
        this.roleRepository = roleRepository;
    }

    @RequestMapping(method = RequestMethod.POST)
    public String add(@Valid @ModelAttribute("user") User user,
                      BindingResult bindingResult,
                      HttpServletRequest request,
                      RedirectAttributes redirectAttrs) {
        User user2 = userRepository.findOne(user.getId());

        if (user2 != null){
            user.setPassword(user2.getPassword());
            user.setActive(user2.getActive());
            user.setRoles(user2.getRoles());
            user.setPassword(bCryptPasswordEncoder.encode("dupa"));


        }else {
            //new user
            user.setPassword(bCryptPasswordEncoder.encode("dupa"));
            Role userRole = roleRepository.findByRole("USER");
            user.setRoles(new HashSet<>(Arrays.asList(userRole)));
            user.setActive(1);
        }


        if (!bindingResult.hasErrors()) {
            userRepository.save(user);
            redirectAttrs.addFlashAttribute("message", "Saved");
        } else {
            redirectAttrs.addFlashAttribute("message", "Not saved");
            System.out.println("Your Message: " + bindingResult);
        }

        String referer = request.getHeader("Referer");

        return "redirect:" + referer;
    }

    @RequestMapping(method = RequestMethod.GET)
    public List<User> show() {

        return userRepository.findAll();
    }

    @RequestMapping(value = "/del/{foo}", method = RequestMethod.GET)
    public String del(@PathVariable String foo,
                      Model model,
                      HttpServletRequest request,
                      RedirectAttributes redirectAttrs) {

        User user = userRepository.findOne(Integer.parseInt(foo));
        Boolean state = false;
        try {
            userRepository.delete(user);

        } catch (Exception e) {
            redirectAttrs.addFlashAttribute("message", "Not deleted: " + e.getMessage());
            state = true;
        }

        if (!state) {

            List<User> usersList = userRepository.findAll();
            model.addAttribute("name", usersList);
            redirectAttrs.addFlashAttribute("message", "Deteted");
            return "index";
        } else {
            String referer = request.getHeader("Referer");

            return "redirect:" + referer;
        }
    }

    @RequestMapping(value = "/more/{foo}", method = RequestMethod.GET)
    public String more(@PathVariable String foo,
                       Model model,
                       HttpServletRequest request,
                       RedirectAttributes redirectAttrs) {
        User user = userRepository.findOne(Integer.parseInt(foo));
        List<Rent> rents = rentsRepository.findByUser_id(Integer.parseInt(foo));

        model.addAttribute("name", user);
        model.addAttribute("rents", rents);

        return "user";

    }
    private String hashToMD5(String pass){
        String passwordToHash = pass;
        String generatedPassword = null;
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(passwordToHash.getBytes());
            byte[] bytes = md.digest();
            StringBuilder sb = new StringBuilder();
            for(int i=0; i< bytes.length ;i++)
            {
                sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
            }

            generatedPassword = sb.toString();
        }
        catch (NoSuchAlgorithmException e)
        {
            e.printStackTrace();
        }
        return generatedPassword;
    }
}

