package com.tjoeun.shop.service;

import java.util.Optional;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.tjoeun.shop.entity.Member;
import com.tjoeun.shop.repository.MemberRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MemberService {

	private final MemberRepository memberRepository;
	
	public Member saveMember(Member member) {
		validateDuplicatedMember(member);
		return memberRepository.save(member);
	}
	
	// 이미 가입한 회원이 있는지 확인하기
	public void validateDuplicatedMember(Member member) {
		
		// argument 로 넣은 이메일 중복 검사하기
		Optional<Member> foundMember = memberRepository.findByEmail(member.getEmail());
	
		if(foundMember.isPresent()) {
			System.out.println(" 이미 가입된 이메일 입니다.");
			throw new IllegalStateException();
		}
	}
	

	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		
		// loadUserByUsername 메서드를 사용하여 사용자의 이메일을 데이터베이스에서 검색하고, 그 결과를 UserDetails 객체로 반환합니다.
		Member member = memberRepository.findByEmail(email).orElseThrow(()-> new UsernameNotFoundException(email+"은 사용 가능합니다."));
		
		return User.builder().username(member.getName()).password(member.getPassword())
				.roles(member.getRole().toString()).build();
	}
	
	
	// 이메일 중복확인 하고 프론트 엔드로 메세지 표시 
	public boolean isEmailAvailable(String email) {
		return !memberRepository.existsByEmail(email);
	}
	
}


