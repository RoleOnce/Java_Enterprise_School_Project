package org.roleonce.projektarbete_web_services.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.roleonce.projektarbete_web_services.model.CustomUser;
import org.roleonce.projektarbete_web_services.model.Movie;
import org.roleonce.projektarbete_web_services.repository.MovieRepository;
import org.roleonce.projektarbete_web_services.repository.UserRepository;
import org.roleonce.projektarbete_web_services.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class UserControllerTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private UserService userService;
    @Mock
    private MovieRepository movieRepository;
    @Mock
    private Model model;
    @Mock
    private BindingResult bindingResult;
    @Mock
    private HttpServletRequest request;
    @Mock
    private Authentication authentication;
    @Mock
    private SecurityContext securityContext;
    @Mock
    private HttpSession session;

    private UserController userController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        userController = new UserController(userRepository, passwordEncoder, userService, movieRepository);
        when(request.getSession()).thenReturn(session);
    }

    @Test
    void testHomeEndpoint() {
        ArrayList<Movie> movies = new ArrayList<>();
        when(movieRepository.findAll()).thenReturn(movies);

        String viewName = userController.home(model);

        assertEquals("home", viewName);              // Assert 1
        verify(model).addAttribute("movies", movies);   // Assert 2
    }

    @Test
    void testRegisterGetEndpoint() {

        String viewName = userController.registerUser(model);

        assertEquals("register", viewName);                         // Assert 3
        verify(model).addAttribute(eq("customUser"), any(CustomUser.class));  // Assert 4
    }

    @Test
    void testRegisterUserWithValidCredentials() {

        CustomUser user = new CustomUser("testUser", "password123");

        when(bindingResult.hasErrors()).thenReturn(false);
        when(userRepository.findByUsername(any())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(any())).thenReturn("encodedPassword");

        String viewName = userController.registerUser(user, bindingResult, model);

        assertEquals("redirect:/", viewName);      // Assert 5
        verify(userRepository).save(any(CustomUser.class));        // Assert 6
    }

    @Test
    void testRegisterUserWithExistingUsername() {

        CustomUser user = new CustomUser("existingUser", "password123");

        when(bindingResult.hasErrors()).thenReturn(false);
        when(userRepository.findByUsername("existingUser")).thenReturn(Optional.of(user));

        String viewName = userController.registerUser(user, bindingResult, model);

        assertEquals("register", viewName);                                                 // Assert 7
        verify(model).addAttribute("usernameError", "Username is already taken"); // Assert 8
    }

    @Test
    void testDeleteUser() {

        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        when(authentication.getName()).thenReturn("testUser");

        String viewName = userController.deleteUser(request);

        assertEquals("redirect:/login?deleted=true", viewName);    // Assert 9
        verify(userService).deleteUser("testUser");                      // Assert 10
    }
}