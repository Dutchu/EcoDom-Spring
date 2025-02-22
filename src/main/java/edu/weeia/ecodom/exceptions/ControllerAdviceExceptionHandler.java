package edu.weeia.ecodom.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class ControllerAdviceExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler({ResourceNotFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleNotFoundException(Exception ex, Model model) {
        model.addAttribute("message", "An unexpected error occurred." + ex.getMessage());
        logger.error("Resource not found error occurred! ", ex); // Logs the stack trace
        return "error/404";
    }
}
