package com.sh.project_JPA.service;

import com.sh.project_JPA.dto.AuthDTO;
import com.sh.project_JPA.dto.UserDTO;
import com.sh.project_JPA.entity.Auth;
import com.sh.project_JPA.entity.User;

import java.util.List;

public interface UserService {


    default User convertUserDTOToUser(UserDTO userDTO){
        return User.builder()
                .email(userDTO.getEmail())
                .pwd(userDTO.getPwd())
                .nickName(userDTO.getNickName())
                .lastLogin(userDTO.getLastLogin())
                .build();
    }

    default Auth converUserDTOToAuth(UserDTO userDTO){
        return Auth.builder()
                .email(userDTO.getEmail())
                .auth("USER_ROLE")
                .build();
    }

    default AuthDTO convertAuthToAuthDTO(Auth auth){
        return AuthDTO.builder()
                .email(auth.getEmail())
                .auth(auth.getAuth())
                .build();
    }

    default UserDTO convertUserToUserDTO(User user, List<AuthDTO> authDTOList){
        return  UserDTO.builder()
                .email((user.getEmail()))
                .pwd((user.getPwd()))
                .nickName(user.getNickName())
                .lastLogin((user.getLastLogin()))
                .regAt((user.getRegAt()))
                .modAt((user.getModAt()))
                .authList(authDTOList)
                .build();
    }






    String join(UserDTO userDTO);

    UserDTO selectEmail(String username);

    boolean updateLastLogin(String authEmail);

    String modifyNoPwd(UserDTO userDTO);

    String modify(UserDTO userDTO);

    void delete(String email);

    List<UserDTO> getList();
}
