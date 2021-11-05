package com.dodonov.oogosu.config.security;

import com.dodonov.oogosu.repository.PrincipalRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

import static org.apache.commons.lang3.BooleanUtils.isTrue;

@Service
@Primary
public class UserDetailsServiceImpl implements UserDetailsService {


    private PrincipalRepository principalRepository;

    private BCryptPasswordEncoder encoder;

    public UserDetailsServiceImpl(PrincipalRepository principalRepository, @Lazy BCryptPasswordEncoder encoder) {
        this.principalRepository = principalRepository;
        this.encoder = encoder;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        var user = principalRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("Username: " + username + " not found"));
        if (isTrue(user.getArchived())){
            throw new RuntimeException("Этот пользователь архивирован, вход невозможен");
        }
        List<GrantedAuthority> grantedAuthorities = AuthorityUtils
                .commaSeparatedStringToAuthorityList("ROLE_" + user.getRole().name());

        // The "User" class is provided by Spring and represents a model class for user to be returned by UserDetailsService
        // And used by auth manager to verify and check user authentication.
        return new User(user.getUsername(), encoder.encode(user.getPassword()), grantedAuthorities);
    }
}