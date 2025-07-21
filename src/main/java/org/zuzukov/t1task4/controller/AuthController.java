package org.zuzukov.t1task4.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.zuzukov.t1task4.dto.*;
import org.zuzukov.t1task4.service.UserService;

import javax.naming.AuthenticationException;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody UserDto userDto) {
        String userId = userService.addUser(userDto);
        return ResponseEntity.ok("User registered with ID: " + userId);
    }

    @PostMapping("/login")
    public ResponseEntity<JwtAuthenticationDto> login(@RequestBody UserCredentiallsDto credentialsDto)
            throws AuthenticationException {
        JwtAuthenticationDto tokens = userService.singIn(credentialsDto);
        return ResponseEntity.ok(tokens);
    }

    @PostMapping("/refresh")
    public ResponseEntity<JwtAuthenticationDto> refresh(@RequestBody RefreshTokenDto refreshTokenDto)
            throws Exception {
        JwtAuthenticationDto newTokens = userService.refreshToken(refreshTokenDto);
        return ResponseEntity.ok(newTokens);
    }

    @GetMapping("/me/{email}")
    public ResponseEntity<UserDto> getUser(@PathVariable String email)
            throws ChangeSetPersister.NotFoundException {
        return ResponseEntity.ok(userService.getUserByEmail(email));
    }
}
