package com.demo.thymeleaf.controller;

import com.demo.thymeleaf.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

@Controller
@Slf4j
public class IndexController {

    @GetMapping(value = {"","/"})
    public ModelAndView index(HttpServletRequest request){
        ModelAndView mv = new ModelAndView();

        User user = (User) request.getSession().getAttribute("user");
        if(user == null){
            mv.setViewName("redirect:/user/login");
        } else {
            mv.addObject(user);
            mv.setViewName("page/index");
        }
        return mv;
    }
}
