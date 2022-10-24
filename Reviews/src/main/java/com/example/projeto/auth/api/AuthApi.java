package com.example.projeto.auth.api;

import static java.lang.String.format;
import static java.util.stream.Collectors.joining;

import java.time.Instant;

import javax.validation.Valid;

import com.example.projeto.usermanagement.api.CreateUserRequest;
import com.example.projeto.usermanagement.api.UserView;
import com.example.projeto.usermanagement.api.UserViewMapper;
import com.example.projeto.usermanagement.models.User;
import com.example.projeto.usermanagement.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * Based on https://github.com/Yoh0xFF/java-spring-security-example
 *
 */
@Tag(name = "Authentication")
@RestController
@RequestMapping(path = "api/public")
public class AuthApi {

    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtEncoder jwtEncoder;
    @Autowired
    private UserViewMapper userViewMapper;
    @Autowired
    private UserService userService;

    @PostMapping("login")
    public ResponseEntity<UserView> login(@RequestBody @Valid final AuthRequest request) {
        try {
            final Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));

            final User user = (User) authentication.getPrincipal();

            final Instant now = Instant.now();
            final long expiry = 36000L;

            final String scope = authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority)
                    .collect(joining(" "));

            final JwtClaimsSet claims = JwtClaimsSet.builder().issuer("example.io").issuedAt(now)
                    .expiresAt(now.plusSeconds(expiry)).subject(format("%s,%s", user.getUserId(), user.getUsername()))
                    .claim("roles", scope).build();

            final String token = this.jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();

            return ResponseEntity.ok().header(HttpHeaders.AUTHORIZATION, token).body(userViewMapper.toUserView(user));
        } catch (final BadCredentialsException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @PostMapping("register")
    public UserView register(@RequestBody @Valid final CreateUserRequest request) {
        return userService.create(request);
    }

}

