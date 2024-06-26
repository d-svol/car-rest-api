package org.example.controller;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(Exception.class)
    public String handleException(Exception e, RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("errorMessage", "Error: " + e.getMessage());
        return "redirect:/error";
    }
}
