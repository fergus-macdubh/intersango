package com.luxoft.alpha.intersango.controllers;

import com.luxoft.alpha.intersango.domain.User;
import com.luxoft.alpha.intersango.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;

@Component
public class ControllerUtils {
    @Autowired
    private UserRepository userRepository;

    public void setUserData(ModelAndView model) {
        model.addObject("currentUser", getCurrentUser());
    }

    public User getCurrentUser() {
        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.getUserByUsername(name);
    }

    public ModelAndView generateError(String message) {
        ModelAndView model = new ModelAndView("error");
        model.addObject("message", message);
        return model;
    }
}
