package org.roleonce.projektarbete_web_services.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class TestController {

    @GetMapping("/login")
    public String test() {
        return "login";
    }

}
