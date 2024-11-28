package com.sh.project_JPA.security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Slf4j
@Component
public class LoginFailureHandler extends SimpleUrlAuthenticationFailureHandler {
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        String errorMessage;
        if(exception instanceof BadCredentialsException){
            errorMessage = "아이디 또는 비밀번호가 맞지않습니다. 다시 확인해주세요.";
        } else if (exception instanceof InternalAuthenticationServiceException){
            errorMessage = "내부 시스템 문제로 로그인 처리를 할 수 없습니다. 관리자에게 문의해주세요.";
        } else if (exception instanceof UsernameNotFoundException){
            errorMessage = "존재하지않는 아이디입니다.";
        } else if (exception instanceof AuthenticationCredentialsNotFoundException){
            errorMessage = "인증요청이 거부됐습니다. 관리자에게 문의해주세요.";
        } else {
            errorMessage = "관리자에게 문의해주세요.";
        }

        errorMessage = URLEncoder.encode(errorMessage, StandardCharsets.UTF_8);
        setDefaultFailureUrl("/user/login?error=true&exception="+errorMessage);
        super.onAuthenticationFailure(request, response, exception);
    }
}
