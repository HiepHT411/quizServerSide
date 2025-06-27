package com.hiephoang.platform.service;

import com.hiephoang.platform.model.User;
import com.hiephoang.platform.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@RequiredArgsConstructor
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserService userService;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userService.getUserByEmail(email)
                .orElse(userService.getUserByUsername(email)
                .orElseThrow(() -> new UsernameNotFoundException(String.format("Username %s not found", email))));
        List<SimpleGrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority(user.getRole()));
        return mapUserToUserDetails(user, authorities);
    }

    private UserDetailsImpl mapUserToUserDetails(User user, List<SimpleGrantedAuthority> authorities) {
        UserDetailsImpl userDetails = new UserDetailsImpl();
        userDetails.setId(user.getId());
        userDetails.setUsername(user.getUsername());
        userDetails.setEmail(user.getEmail());
        userDetails.setPassword(user.getPassword());
        userDetails.setAuthorities(authorities);
        return userDetails;
    }
}
