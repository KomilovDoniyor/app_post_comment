package test.app_post_comment.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import test.app_post_comment.domain.User;
import test.app_post_comment.repository.UserRepository;

import java.util.Collection;
import java.util.Collections;

@Service
@RequiredArgsConstructor
public class UserDetailServiceImpl implements UserDetailsService {
    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found username: " + username));
        return new org.springframework.security.core.userdetails.User(
                username,
                user.getPassword(),
                user.isEnable(),
                true,
                true,
                true,
                getUserAuthorities("USER")
        );
    }

    private Collection<? extends GrantedAuthority> getUserAuthorities(String role) {
        return Collections.singleton(new SimpleGrantedAuthority(role));
    }
}
