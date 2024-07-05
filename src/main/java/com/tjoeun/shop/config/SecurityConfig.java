package com.tjoeun.shop.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
		
		// 기본적인 로그인 처리기능 http.formLogin(Customizer.withDefaults())
		// 다음은 로그인처리 커스터마이징
		http.formLogin(form->form.loginPage("/member/signin")
				.defaultSuccessUrl("/",true) // 로그인 성공시 항상 /로 리디렉션한다는 의미
				.failureUrl("/member/signin/error")
				.usernameParameter("email")
				.permitAll()); // 로그인 페이지 접근을 모든 사용자에게 허용한다는 의미
		
		// 기본적인 로그아웃 기능 http.logout(Customizer.withDefaults()); 
		// 로그아웃 커스터마이징
		http.logout(logout->logout.logoutUrl("/logout")
				.logoutSuccessUrl("/?logout")
				.permitAll()); // 로그아웃 페이지 접근을 모든 사용자에게 허용
		
		// 페이지에 대한 접근권한 설정
		http.authorizeHttpRequests(request->request.requestMatchers("/css/**").permitAll()
				.requestMatchers("/","/member/**").permitAll()
				.requestMatchers("/admin/**").hasRole("ADMIN")
				.anyRequest().authenticated());
		
		http.exceptionHandling(exception->exception
				.authenticationEntryPoint(new CustomAuthenticationEntryPoint()));
		
		return http.build();
		
	}
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		
		return new BCryptPasswordEncoder();
		
	}
	
}
