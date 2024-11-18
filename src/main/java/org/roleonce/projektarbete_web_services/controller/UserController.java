package org.roleonce.projektarbete_web_services.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.roleonce.projektarbete_web_services.model.CustomUser;
import org.roleonce.projektarbete_web_services.repository.UserRepository;
import org.roleonce.projektarbete_web_services.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class UserController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserService userService;

    @Autowired
    public UserController(UserRepository userRepository, PasswordEncoder passwordEncoder, UserService userService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.userService = userService;
    }

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping("/register")
    public String registerUser(Model model) {

        model.addAttribute("customUser", new CustomUser());

        return "register";
    }

    @PostMapping("/register")
    public String registerUser(
            @Valid @ModelAttribute(name = "customUser") CustomUser customUser,
            BindingResult bindingResult,
            Model model
    ) {
        if (bindingResult.hasErrors()) {
            return "register";
        }

        if (userRepository.findByUsername(customUser.getUsername()).isPresent()) {
            model.addAttribute("usernameError", "Username is already taken");
            return "register";
        }

        try {
            userRepository.save(
                    new CustomUser(
                            customUser.getUsername(),
                            passwordEncoder.encode(customUser.getPassword()

                            )));
        } catch (DataIntegrityViolationException e) {
            model.addAttribute("usernameError", "Användarnamnet är redan taget.");
            return "register";
        }


        return "redirect:/";
    }

    @GetMapping("/delete-user")
    public String showDeleteUserPage() {
        return "delete-user";
    }

    // Hanterar den faktiska borttagningen av användaren
    @PostMapping("/delete-user")
    public String deleteUser(HttpServletRequest request) {
        // Hämta den inloggade användaren
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        // Ta bort användaren
        userService.deleteUser(username);

        // Logga ut användaren
        SecurityContextHolder.clearContext();
        request.getSession().invalidate();

        // Redirecta till startsidan eller inloggningssidan
        return "redirect:/login?deleted=true";
    }

}
