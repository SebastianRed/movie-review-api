package cl.sebastianrojo.moviereview.controller;

import java.util.List;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cl.sebastianrojo.moviereview.dto.AuthRequest;
import cl.sebastianrojo.moviereview.dto.AuthResponse;
import cl.sebastianrojo.moviereview.entity.Role;
import cl.sebastianrojo.moviereview.entity.User;
import cl.sebastianrojo.moviereview.repository.UserRepository;
import cl.sebastianrojo.moviereview.security.JwtUtil;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:5173")
public class AuthController {

    private final AuthenticationManager authenticationManager;

    private final JwtUtil jwtUtil;

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    public AuthController(AuthenticationManager authenticationManager, JwtUtil jwtUtil, UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/login")
    public AuthResponse login(@RequestBody AuthRequest request) {
        Authentication auth = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.username(), request.password()));
        UserDetails userDetails = (UserDetails) auth.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream().map(a -> a.getAuthority()).toList();
        String token = jwtUtil.generateToken(userDetails.getUsername(), roles);
        return new AuthResponse(token);
    }

    @PostMapping("/register")
    public void register(@RequestBody AuthRequest request) {
        if(userRepository.existsByUsername(request.username())) {
            throw new RuntimeException("El nombre de usuario ya existe");
        }
        User user = new User(
            request.username(), 
            passwordEncoder.encode(request.password()), 
            request.username() + "@mail.com", 
            Role.USER
        );
        userRepository.save(user);
    }
}