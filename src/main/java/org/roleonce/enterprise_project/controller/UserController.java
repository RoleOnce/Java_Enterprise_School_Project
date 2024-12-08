package org.roleonce.enterprise_project.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.roleonce.enterprise_project.authorities.UserRole;
import org.roleonce.enterprise_project.dao.UserDAO;
import org.roleonce.enterprise_project.model.*;
import org.roleonce.enterprise_project.repository.MovieRepository;
import org.roleonce.enterprise_project.repository.UserRepository;
import org.roleonce.enterprise_project.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class UserController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserService userService;
    private final MovieRepository movieRepository;
    private final WebClient webClient;
    private final UserDAO userDAO;

    @Autowired
    public UserController(UserRepository userRepository, PasswordEncoder passwordEncoder, UserService userService, MovieRepository movieRepository, WebClient.Builder webclientBuilder, UserDAO userDAO) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.userService = userService;
        this.movieRepository = movieRepository;
        this.webClient = webclientBuilder.build();
        this.userDAO = userDAO;
    }

    @Value("${API_KEY}")
    private String apiKey;
    final String apiUrl = "https://api.themoviedb.org/3/movie/";

    @GetMapping("/")
    public String home(Model model) {
        List<Movie> movies = movieRepository.findAll();
        model.addAttribute("movies", movies);
        return "home";
    }

    @GetMapping("/register")
    public String registerUser(Model model) {

        model.addAttribute("userDTO", new UserDTO("", "", UserRole.USER));
        model.addAttribute("roles", UserRole.values());

        return "register";
    }

    @PostMapping("/register")
    public String registerUser(
            @Valid @ModelAttribute(name = "userDTO") UserDTO userDTO,
            BindingResult bindingResult,
            Model model
    ) {
        if (bindingResult.hasErrors()) {

            model.addAttribute("roles", UserRole.values());

            return "register";
        }

        if (userRepository.findByUsername(userDTO.username()).isPresent()) {

            model.addAttribute("usernameError", "Username is already taken");
            model.addAttribute("roles", UserRole.values());

            return "register";
        }

        try {
            CustomUser newUser = new CustomUser(
                    userDTO.username(),
                    passwordEncoder.encode(userDTO.password()),
                    userDTO.userRole() != null ? userDTO.userRole() : UserRole.USER, // Sätt en standard roll om ingen anges
                    true,
                    true,
                    true,
                    true
            );

            userRepository.save(newUser);

        } catch (DataIntegrityViolationException e) {

            model.addAttribute("usernameError", "Username is already taken.");
            model.addAttribute("roles", UserRole.values());

            return "register";
        }
        return "redirect:/";
    }

    /*
    @GetMapping("/delete-user")
    public String showDeleteUserPage() {

        return "delete-user";
    }

    @PostMapping("/delete-user")
    public String deleteUser(HttpServletRequest request) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        userService.deleteUser(username);
        SecurityContextHolder.clearContext();
        request.getSession().invalidate();

        return "redirect:/login?deleted=true";
    }
     */

    @GetMapping("/deleteuser")
    public String deleteUserGet() {

        return "deleteuser";
    }

    @PostMapping("/deleteuser")
    public String deleteUser(@RequestParam Long id, Model model) {

        try {
            userDAO.deleteById(id);
            model.addAttribute("successMessage", "User with id " + id + " has been deleted.");
        } catch (EmptyResultDataAccessException e) {
            model.addAttribute("errorMessage", "User with id " + id + " not found.");
        } catch (Exception e) {
            model.addAttribute("errorMessage", "An error occurred while deleting the user.");
        }

        return "redirect:/";
    }

    @GetMapping("/save-movie")
    public String showSaveMoviePage(Model model) {

        model.addAttribute("movieDTO", new MovieDTO(null));

        return "save-movie";
    }

    @PostMapping("/save-movie")
    public String saveMovie(@ModelAttribute("movieDTO") MovieDTO movieDTO,
                            Model model,
                            RedirectAttributes redirectAttributes) {
        try {
            String url = apiUrl + movieDTO.movieId() + "?api_key=" + apiKey;

            Movie movie = webClient.get()
                    .uri(url)
                    .retrieve()
                    .bodyToMono(Movie.class)
                    .block();

            if (movie == null) {
                model.addAttribute("errorMessage", "Movie not found.");
                return "save-movie";
            }

            if (movie.getOverview().length() > 254) {
                movie.setOverview(movie.getOverview().substring(0, 254));
            }

            Movie existingMovie = movieRepository.findById(movie.getId()).orElse(null);
            if (existingMovie != null) {
                model.addAttribute("errorMessage", "Movie with this ID already exists in the database.");
                return "save-movie";
            }

            movie.setId(null);
            movieRepository.save(movie);

            model.addAttribute("successMessage", "Movie saved successfully!");

            return "save-movie";

        } catch (Exception e) {

            model.addAttribute("errorMessage", "Error saving movie: " + e.getMessage());

            return "save-movie";
        }
    }

}
