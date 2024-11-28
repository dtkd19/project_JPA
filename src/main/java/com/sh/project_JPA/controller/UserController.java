package com.sh.project_JPA.controller;

import com.sh.project_JPA.dto.UserDTO;
import com.sh.project_JPA.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/user/*")
@Slf4j
public class UserController {
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    @GetMapping("/join")
    public void join() {}

    @PostMapping("/join")
    public String join(UserDTO userDTO){
        userDTO.setPwd(passwordEncoder.encode(userDTO.getPwd()));
        String email = userService.join(userDTO);
        log.info("email >> {} " , email);
        return (email == null) ? "/user/join" : "/index";
    }

    @GetMapping("/login")
    public void login(@RequestParam(value = "error", required = false) String error,
                      @RequestParam(value = "exception", required = false) String exception,
                      Model model) {
        /* 에러와 예외값을 담아 화면으로 전달*/
        model.addAttribute("error",error);
        model.addAttribute("exception", exception);
    }

    @GetMapping("/mypage")
    public void modify() {}

    @PostMapping("/mypage")
    public String mypage(UserDTO userDTO){
        log.info(">>> mypage UserDTO >> {}" ,userDTO);

        String email;
        if(userDTO.getPwd() == null || userDTO.getPwd().isEmpty()) {
            // 비번 없이 업데이트 진행
            email = userService.modifyNoPwd(userDTO);
        }else {
            // 비번 암호화 후 업데이트 진행
            userDTO.setPwd(passwordEncoder.encode(userDTO.getPwd()));
            email = userService.modify(userDTO);
        }

        return "redirect:/user/logout";
    }

    @GetMapping("/delete")
    public String delete(Principal principal){
        String email = principal.getName();

        userService.delete(email);

        return "redirect:/user/logout";
    }

    @GetMapping("/list")
    public void list(Model model){

        List<UserDTO> userList = userService.getList();

        model.addAttribute("userList", userList);
    }
}
