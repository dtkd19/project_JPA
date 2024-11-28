package com.sh.project_JPA.security;

import com.sh.project_JPA.dto.UserDTO;
import com.sh.project_JPA.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@Slf4j
public class CustomUserService implements UserDetailsService {
    @Autowired
    public UserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserDTO userDTO = userService.selectEmail(username);
        log.info(">>> login User >> {} ", userDTO);
        if(userDTO == null){
            throw new UsernameNotFoundException(username);
        }
        return new AuthMember(userDTO);
    }
}
