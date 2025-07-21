package org.zuzukov.t1task4.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.zuzukov.t1task4.repository.UserRepository;

public class CustomUserServiceImp implements UserDetailsService {
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByEmail(username).map(CustomUserDetail::new).orElseThrow(

                () -> new UsernameNotFoundException(username)
        );
    }
}
