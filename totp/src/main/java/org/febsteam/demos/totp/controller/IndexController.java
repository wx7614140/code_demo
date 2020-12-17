package org.febsteam.demos.totp.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author : wx
 * @version : 1
 * @date : 2020/12/17 10:15
 */
@Controller
@RequiredArgsConstructor
@RequestMapping("2fa")
public class IndexController {

    @GetMapping("index")
    @ResponseBody
    public ModelAndView index(HttpServletRequest request, HttpServletResponse response) {
        ModelAndView mv=new ModelAndView();
        mv.setViewName("/bind");
        return mv;
    }
}
