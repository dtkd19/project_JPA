package com.sh.project_JPA.service;

import com.sh.project_JPA.dto.AuthDTO;
import com.sh.project_JPA.dto.UserDTO;
import com.sh.project_JPA.entity.Auth;
import com.sh.project_JPA.entity.User;
import com.sh.project_JPA.repository.AuthRepository;
import com.sh.project_JPA.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final AuthRepository authRepository;

    @Override
    public String join(UserDTO userDTO) {
        String email = userRepository.save(convertUserDTOToUser(userDTO)).getEmail();
        if( email != null ){
            authRepository.save(converUserDTOToAuth(userDTO));
        }
        return email;
    }

    @Transactional
    @Override
    public UserDTO selectEmail(String username) {
        Optional<User> optional = userRepository.findById(username);
        List<Auth> authList = authRepository.findByEmail(username);

        if(optional.isPresent()){
            UserDTO userDTO = convertUserToUserDTO(optional.get(),
                    authList.stream().map(a -> convertAuthToAuthDTO(a)).toList()
            );
            log.info(" login User >>> {} ", userDTO);
            return userDTO;
        }

        return null;
    }

    @Override
    public boolean updateLastLogin(String authEmail) {
        Optional<User> optional = userRepository.findById(authEmail);

        if(optional.isPresent()){
            User user = optional.get();
            user.setLastLogin(LocalDateTime.now());
            String email = userRepository.save(user).getEmail();
            return email == null ? false : true;
        }
        return false;
    }

    @Override
    public String modifyNoPwd(UserDTO userDTO) {
        log.info(">>> userDTO Email >> {}" , userDTO.getEmail());
        Optional<User> optional = userRepository.findById(userDTO.getEmail());
        log.info( " >>> optional >> {} ", optional.isPresent());
        if(optional.isPresent()){
            User user = optional.get();
            user.setNickName(userDTO.getNickName());
            return userRepository.save(user).getEmail();
        }
        return null;
    }

    @Override
    public String modify(UserDTO userDTO) {
        Optional<User> optional = userRepository.findById(userDTO.getEmail());
        if(optional.isPresent()){
            User user = optional.get();
            user.setNickName(userDTO.getNickName());
            user.setPwd(userDTO.getPwd());
            return userRepository.save(user).getEmail();
        }
        return null;
    }

    @Override
    public void delete(String email) {
        Optional<User> optional = userRepository.findById(email);
        if(optional.isPresent()){
            User user = optional.get();
            List<Auth> authList = authRepository.findByEmail(user.getEmail());
            for(Auth auth : authList){
                authRepository.deleteById(auth.getId());
            }
        }
        userRepository.deleteById(email);
    }

    @Override
    public List<UserDTO> getList() {
        List<User> userList = userRepository.findAll();

        for (User user : userList) {
            if (user.getEmail() != null) {
                List<Auth> authList = authRepository.findByEmail(user.getEmail());
                List<AuthDTO> authDTOList = authList.stream().map(a -> convertAuthToAuthDTO(a)).toList();
                List<UserDTO> userDTOList = userList.stream().map(u -> convertUserToUserDTO(u, authDTOList)).toList();
                return userDTOList;
            }
        }
        return null;
    }
}

