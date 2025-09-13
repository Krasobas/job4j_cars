package ru.job4j.cars.controller;

import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.job4j.cars.dto.notification.NotificationDto;
import ru.job4j.cars.dto.user.UserSessionDto;
import ru.job4j.cars.service.notification.NotificationService;

import java.util.List;

@Controller
@RequestMapping("/notifications")
@AllArgsConstructor
public class NotificationController {
    private final NotificationService service;

    @DeleteMapping("/{id}")
    public String delete(@PathVariable(name = "id") Long id, Model model, HttpSession session) {
        if (!service.delete(id)) {
            model.addAttribute("error", "Notification not found.");
            return "redirect:/error/404";
        }
        UserSessionDto user = (UserSessionDto) session.getAttribute("user");
        if (user != null && user.getId() != null) {
            List<NotificationDto> updatedNotifications = service.findByUserId(user);
            session.setAttribute("notifications", updatedNotifications);
        }
        return "redirect:/posts";
    }
}
