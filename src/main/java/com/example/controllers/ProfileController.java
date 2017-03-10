package com.example.controllers;

import com.example.models.Rent;
import com.example.models.Role;
import com.example.models.User;
import com.example.repository.RentsRepository;
import com.example.repository.RoleRepository;
import com.example.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

/**
 * Created by PatrykGrudnik on 08.03.2017.
 */
@Controller
@RequestMapping(value = "/profile")
public class ProfileController {
    public final RoleRepository roleRepository;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;


    @Autowired
    UserRepository userRepository;
    @Autowired
    RentsRepository rentsRepository;

    public ProfileController(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @RequestMapping(method = RequestMethod.GET)
    public String show(Model model) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        boolean admin=false;
        User user = userRepository.findByMail(auth.getName());
        Role userRole = roleRepository.findByRole("ADMIN");
        Object o = new HashSet<>(Arrays.asList(userRole));
        if(user.getRoles().equals(o))
            admin=true;

        List<Rent> rents = rentsRepository.findByUser_id(user.getId());

        model.addAttribute("name", user);
        model.addAttribute("rents", rents);
        model.addAttribute("anycar",!rents.isEmpty());
        model.addAttribute("userName", user.getName());
        model.addAttribute("role", admin);

        return "profile";
    }
    @RequestMapping(value = "/edit",method = RequestMethod.POST)
    public String add(@Valid @ModelAttribute("user") User user,
                      BindingResult bindingResult,
                      HttpServletRequest request,
                      RedirectAttributes redirectAttrs) {
        User user2 = userRepository.findOne(user.getId());

        if (user2 != null){

            user.setActive(user2.getActive());
            user.setRoles(user2.getRoles());
            user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        }

        if (!bindingResult.hasErrors()) {
            userRepository.save(user);
            redirectAttrs.addFlashAttribute("message", "Saved");
        } else {
            redirectAttrs.addFlashAttribute("message", "Not saved");
            System.out.println("Your Message : " + bindingResult);
        }

        String referer = request.getHeader("Referer");

        return "redirect:" + referer;
    }

}
