package com.diedari.jimdur.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ErrorController {

    @GetMapping("/error/403")
    public String error403() {
        return "error/403"; // ruta a la plantilla Thymeleaf
    }
}
