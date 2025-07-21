package org.zuzukov.t1task4.service;


import org.springframework.data.crossstore.ChangeSetPersister;
import org.zuzukov.t1task4.dto.JwtAuthenticationDto;
import org.zuzukov.t1task4.dto.RefreshTokenDto;
import org.zuzukov.t1task4.dto.UserCredentiallsDto;
import org.zuzukov.t1task4.dto.UserDto;

import javax.naming.AuthenticationException;

public interface UserService {
    JwtAuthenticationDto singIn(UserCredentiallsDto userCredentialsDto) throws AuthenticationException;
    JwtAuthenticationDto refreshToken(RefreshTokenDto refreshTokenDto) throws Exception;
    UserDto getUserById(String id) throws ChangeSetPersister.NotFoundException;
    UserDto getUserByEmail(String email) throws ChangeSetPersister.NotFoundException;
    String addUser(UserDto user);
     void revokeToken(String token);

}