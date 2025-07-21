package org.zuzukov.t1task4.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.zuzukov.t1task4.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class CustomUserServiceImp implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userRepository.findByEmail(email)
                .map(CustomUserDetail::new)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + email));
    }
}
