package cl.sebastianrojo.moviereview.service;

import cl.sebastianrojo.moviereview.dto.auth.AuthResponse;
import cl.sebastianrojo.moviereview.dto.auth.LoginRequest;
import cl.sebastianrojo.moviereview.dto.auth.RegisterRequest;
import cl.sebastianrojo.moviereview.entity.Role;
import cl.sebastianrojo.moviereview.entity.User;
import cl.sebastianrojo.moviereview.exception.DuplicateResourceException;
import cl.sebastianrojo.moviereview.repository.UserRepository;
import cl.sebastianrojo.moviereview.security.JwtUtil;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    public AuthService(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            AuthenticationManager authenticationManager,
            JwtUtil jwtUtil
    ) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
    }

    /**
     * Registra un nuevo usuario
     */
    @Transactional
    public AuthResponse register(RegisterRequest request) {
        // Validar si el username ya existe
        if (userRepository.existsByUsername(request.username())) {
            throw new DuplicateResourceException("El nombre de usuario ya está en uso");
        }

        // Validar si el email ya existe
        if (userRepository.existsByEmail(request.email())) {
            throw new DuplicateResourceException("El email ya está registrado");
        }

        // Crear nuevo usuario
        User user = new User(
            request.username(),
            request.email(),
            passwordEncoder.encode(request.password()),
            Role.USER
        );

        user = userRepository.save(user);

        // Generar token JWT
        String token = jwtUtil.generateToken(
            user.getUsername(),
            List.of("ROLE_" + user.getRole().name())
        );

        return new AuthResponse(
            token,
            user.getUsername(),
            user.getEmail(),
            user.getRole().name()
        );
    }

    /**
     * Autentica un usuario existente
     */
    public AuthResponse login(LoginRequest request) {
        // Autenticar con Spring Security
        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                request.username(),
                request.password()
            )
        );

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        
        // Obtener información completa del usuario
        User user = userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // Generar token JWT
        List<String> roles = userDetails.getAuthorities().stream()
                .map(authority -> authority.getAuthority())
                .toList();

        String token = jwtUtil.generateToken(userDetails.getUsername(), roles);

        return new AuthResponse(
            token,
            user.getUsername(),
            user.getEmail(),
            user.getRole().name()
        );
    }
}