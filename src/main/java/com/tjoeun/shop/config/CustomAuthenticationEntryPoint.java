package com.tjoeun.shop.config;

import java.io.IOException;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

//사용자가 보호된 리소스에 액세스할 때 인증되지 않은 경우를 처리하기 위한 클래스
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {
	
	// commence 메서드는 사용자가 인증되지않은 요청을 보낼 때 호출됩니다.
	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) 
			throws IOException, ServletException {
		
		// 만약 요청이 ajax인 경우
		if("XMLHTTPRequest".equals(request.getHeader("x-requested-with"))) {
			response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "인증에 실패했습니다 (UNAUTHORIZED");
		} else { // 그렇지 않은경우 로그인페이지로 리다이렉트
			response.sendRedirect("/member/signin");
			
		}
		
	}

}
