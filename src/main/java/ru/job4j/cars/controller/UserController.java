package ru.job4j.cars.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.job4j.cars.dto.user.UserCreateDto;
import ru.job4j.cars.dto.user.UserLoginDto;
import ru.job4j.cars.dto.user.UserSessionDto;
import ru.job4j.cars.model.User;
import ru.job4j.cars.service.notification.NotificationService;
import ru.job4j.cars.service.user.UserService;

import java.util.Optional;

@Controller
@RequestMapping("/users")
@AllArgsConstructor
public class UserController {
    private final UserService userService;
    private  final NotificationService notificationService;

    @GetMapping("/login")
    public String getLoginPage() {
        return "users/login";
    }

    @PostMapping("/login")
    public String login(@ModelAttribute("user") UserLoginDto user, Model model, HttpServletRequest request) {
        Optional<UserSessionDto> found = userService.findByEmailAndPassword(user.getEmail(), user.getPassword());
        if (found.isEmpty()) {
            model.addAttribute("error", "Account not found, please enter a valid email or password");
            return "users/login";
        }
        var session = request.getSession();
        session.setAttribute("user", found.get());
        session.setAttribute("notifications", notificationService.findByUserId(found.get()));
        return "redirect:/posts";
    }

    @GetMapping("/register")
    public String getRegistrationPage(Model model) {
        model.addAttribute("timeZones", userService.listTimeZones());
        return "users/register";
    }

    @PostMapping("/register")
    public String register(@ModelAttribute UserCreateDto user, Model model) {
        if (!userService.save(user)) {
            model.addAttribute("error", "User with this email already exists")
                 .addAttribute("timeZones", userService.listTimeZones());
            return "/users/register";
        }
        return "redirect:/users/login";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }

}
