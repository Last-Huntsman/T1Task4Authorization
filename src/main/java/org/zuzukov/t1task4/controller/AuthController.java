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
//    {
//        "token": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0ZXN0QGV4YW1wbGUuY29tIiwiZXhwIjoxNzUzMDk4MTI3fQ.RJFF0Q2IflndetbR3c3aSmd61TyY_ezykynBlRpTBZ8",
//            "refreshToken": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0ZXN0QGV4YW1wbGUuY29tIiwiZXhwIjoxNzUzMTg0NDY3fQ.NvxpLnwSnVLyk3SekcAAS0TRmqoWO1_NpI0nUN4SE74"
//    }
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
    public ResponseEntity<?> getUser(
            @PathVariable String email,
            @RequestHeader("Authorization") String authorizationHeader)
            throws ChangeSetPersister.NotFoundException {

        String token = authorizationHeader.replace("Bearer ", "");
        if (!userService.validateToken(token, email)) {
            return ResponseEntity
                    .status(401)
                    .body("Unauthorized: Invalid or expired token for given email");
        }

        return ResponseEntity.ok(userService.getUserByEmail(email));
    }


    @PostMapping("/logout")
    public ResponseEntity<String> logout(@RequestBody TokenDto tokenDto) {
        userService.revokeToken(tokenDto.getToken());
        return ResponseEntity.ok("Token has been revoked");
    }

}
