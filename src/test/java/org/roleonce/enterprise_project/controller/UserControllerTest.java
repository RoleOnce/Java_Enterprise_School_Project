package org.roleonce.enterprise_project.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.roleonce.enterprise_project.model.CustomUser;
import org.roleonce.enterprise_project.model.Movie;
import org.roleonce.enterprise_project.model.UserDTO;
import org.roleonce.enterprise_project.repository.MovieRepository;
import org.roleonce.enterprise_project.repository.UserRepository;
import org.roleonce.enterprise_project.service.UserService;
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

@ExtendWith(MockitoExtension.class)
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
        userController = new UserController(userRepository, passwordEncoder, userService, movieRepository);

        SecurityContextHolder.setContext(securityContext);
    }

    @Test
    void testHomeEndpoint() {
        ArrayList<Movie> movies = new ArrayList<>();
        when(movieRepository.findAll()).thenReturn(movies);

        String viewName = userController.home(model);

        assertEquals("index", viewName);
        verify(model).addAttribute("movies", movies);
    }

    @Test
    void testRegisterGetEndpoint() {
        String viewName = userController.registerUser(model);

        assertEquals("register", viewName);
        verify(model).addAttribute(eq("userDTO"), any(UserDTO.class));
    }

    @Test
    void testRegisterUserWithValidCredentials() {
        UserDTO user = new UserDTO("testUser", "password123");

        lenient().when(bindingResult.hasErrors()).thenReturn(false);
        lenient().when(userRepository.findByUsername(any())).thenReturn(Optional.empty());
        lenient().when(passwordEncoder.encode(any())).thenReturn("encodedPassword");

        String viewName = userController.registerUser(user, bindingResult, model);

        assertEquals("redirect:/", viewName);
        verify(userRepository).save(any(CustomUser.class));
    }

    @Test
    void testRegisterUserWithExistingUsername() {
        UserDTO user = new UserDTO("existingUser", "password123");

        lenient().when(bindingResult.hasErrors()).thenReturn(false);
        lenient().when(userRepository.findByUsername("existingUser"))
                .thenReturn(Optional.of(new CustomUser("existingUser", "password123")));

        String viewName = userController.registerUser(user, bindingResult, model);

        assertEquals("register", viewName);
        verify(model).addAttribute("usernameError", "Username is already taken");
    }

    @Test
    void testDeleteUser() {
        // Skapa en mock session explicit
        HttpSession mockSession = mock(HttpSession.class);

        // Se till att getSession() returnerar en mock-session
        when(request.getSession()).thenReturn(mockSession);

        // Förbered SecurityContext
        SecurityContextHolder.setContext(securityContext);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn("testUser");

        String viewName = userController.deleteUser(request);

        assertEquals("redirect:/login?deleted=true", viewName);
        verify(userService).deleteUser("testUser");
        verify(mockSession).invalidate();

        // Rensa SecurityContext efter testet
        SecurityContextHolder.clearContext();
    }
}
