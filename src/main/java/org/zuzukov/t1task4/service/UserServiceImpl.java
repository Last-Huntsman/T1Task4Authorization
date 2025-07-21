package org.zuzukov.t1task4.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.zuzukov.t1task4.dto.*;
import org.zuzukov.t1task4.entity.Role;
import org.zuzukov.t1task4.entity.User;
import org.zuzukov.t1task4.repository.UserRepository;

import javax.naming.AuthenticationException;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

    @Service
    @RequiredArgsConstructor
    public class UserServiceImpl implements UserService {

        private final UserRepository userRepository;
        private final JwtService jwtService;
        private final PasswordEncoder passwordEncoder;

        @Override
        public JwtAuthenticationDto singIn(UserCredentiallsDto userCredentialsDto) throws AuthenticationException {
            Optional<User> userOpt = userRepository.findByEmail(userCredentialsDto.getEmail());
            if (userOpt.isEmpty()) throw new AuthenticationException("User not found");

            User user = userOpt.get();

            if (!passwordEncoder.matches(userCredentialsDto.getPassword(), user.getPassword())) {
                throw new AuthenticationException("Invalid credentials");
            }

            return jwtService.generateJwtAuthenticationDto(user.getEmail());
        }

        @Override
        public JwtAuthenticationDto refreshToken(RefreshTokenDto refreshTokenDto) throws Exception {
            String email = jwtService.getEmailFromToken(refreshTokenDto.getRefreshToken());
            if (!jwtService.validateJwtToken(refreshTokenDto.getRefreshToken())) {
                throw new Exception("Invalid or expired refresh token");
            }
            return jwtService.refreshBaseToken(email, refreshTokenDto.getRefreshToken());
        }

        @Override
        public UserDto getUserById(String id) throws ChangeSetPersister.NotFoundException {
            UUID uuid = UUID.fromString(id);
            User user = userRepository.findByUserId(uuid)
                    .orElseThrow(ChangeSetPersister.NotFoundException::new);
            return mapToDto(user);
        }

        @Override
        public UserDto getUserByEmail(String email) throws ChangeSetPersister.NotFoundException {
            User user = userRepository.findByEmail(email)
                    .orElseThrow(ChangeSetPersister.NotFoundException::new);
            return mapToDto(user);
        }

        @Override
        public String addUser(UserDto userDto) {
            if (userRepository.findByEmail(userDto.getEmail()).isPresent()) {
                throw new RuntimeException("User already exists");
            }

            User user = new User();
            user.setUserId(UUID.randomUUID());
            user.setEmail(userDto.getEmail());
            user.setFirstName(userDto.getFirstName());
            user.setLastName(userDto.getLastName());
            user.setPassword(passwordEncoder.encode(userDto.getPassword()));

            user.setRoles(Set.of(Role.ROLE_GUEST));

            userRepository.save(user);
            return user.getUserId().toString();
        }

        private UserDto mapToDto(User user) {
            UserDto dto = new UserDto();
            dto.setUserId(user.getUserId().toString());
            dto.setFirstName(user.getFirstName());
            dto.setLastName(user.getLastName());
            dto.setEmail(user.getEmail());
            dto.setPassword(user.getPassword());
            return dto;
        }
    }


