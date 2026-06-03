package com.raihan.nexaorder.controller;

import com.raihan.nexaorder.dto.UserResponseDTO;
import com.raihan.nexaorder.enums.UserRole;
import com.raihan.nexaorder.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/admin/users")
    public String adminUsers(Model model) {
        List<UserResponseDTO> users = userService.getAllUsers();

        model.addAttribute("users", users);
        model.addAttribute("adminCount", countByRole(users, UserRole.ADMIN));
        model.addAttribute("customerCount", countByRole(users, UserRole.CUSTOMER));
        return "admin/users";
    }

    private long countByRole(List<UserResponseDTO> users, UserRole role) {
        return users.stream()
                .filter(user -> role.equals(user.getRole()))
                .count();
    }
}
