package com.tjoeun.shop.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.tjoeun.shop.dto.MemberDto;
import com.tjoeun.shop.entity.Member;
import com.tjoeun.shop.service.MemberService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/member")
@RequiredArgsConstructor
public class MemberController {
	
	private final MemberService memberService;
	
	private final PasswordEncoder passwordEncoder;
	
	@GetMapping("/signup")
	public String signup(MemberDto memberDto) {
		return "signup";
	}
	
	@PostMapping("/signup_proc")
	public String signup_proc(@Valid MemberDto memberDto, BindingResult result, Model model,
							  @RequestParam("confirmPassword") String confirmPassword, RedirectAttributes redirectAttributes) {
		
	    // 비밀번호와 비밀번호 확인이 일치하지 않으면 에러를 추가하고 회원가입 페이지로 리다이렉트
        if (!memberDto.getPassword().equals(confirmPassword)) {
            result.rejectValue("password", "error.password", "비밀번호가 일치하지 않습니다.");
            return "signup"; // 회원가입 페이지로 리다이렉트
        }
		
		// 이메일 중복확인
        if (!memberService.isEmailAvailable(memberDto.getEmail())) {
        	result.rejectValue("email", "error.email","이미 사용 중인 이메일 입니다.");
        	model.addAttribute("isAvailable",false);
        	return "signup";
        }
        
        
		if(result.hasErrors()) {
			return "signup";
		}
		
		try {
			Member member = Member.createMember(memberDto, passwordEncoder);   // 회원 객체 생성
			Member savedMember = memberService.saveMember(member);   // 생성된 회원 객체를 저장
		} catch(IllegalStateException e) {   // 회원 객체를 생성하는 과정에서 유효성 검사를 실패할 때 발생
			model.addAttribute("errorMsg",e.getMessage());   // 예외 메세지를 모델에 추가
			return "signup"; 
		}
		
	    redirectAttributes.addFlashAttribute("message", "회원가입이 완료되었습니다.");
		return "redirect:/signin";
		
	}
	
	@GetMapping("/signin")
	public String signin() {
		return "signin";
	}
	
	@GetMapping("/signout")
	public String signout(HttpServletRequest request, HttpServletResponse response,RedirectAttributes redirectAttributes) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication(); // 현재 사용자의 인증정보를 가져오기
		
		// session 삭제하기
		if(authentication !=null) {  // authentication 객체가 null 이 아니면 사용자가 인증되어 있는 것
			new SecurityContextLogoutHandler().logout(request, response, authentication); 
		}
		
		redirectAttributes.addFlashAttribute("message", "로그아웃되었습니다.");
		return "redirect:/";
	}
	
	@GetMapping("/signin/error")
	public String loginError(Model model) {
		model.addAttribute("signinErrorMsg", "아이디나 비밀번호를 다시 확인하세요.");
		return "signin";
	}
	


}
