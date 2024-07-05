package com.tjoeun.shop.entity;

import java.time.LocalDate;

import org.springframework.security.crypto.password.PasswordEncoder;

import com.tjoeun.shop.constant.Role;
import com.tjoeun.shop.dto.MemberDto;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Member {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="member_id")
	private  Long id;
	
	private String name;
	
	@Column(unique=true)
	private String email;
	
	private String password;
	private String address;
	
	private LocalDate  birth;
	
	@Enumerated(EnumType.STRING)
	private Role role;
	
	public static Member createMember(MemberDto memberDto, PasswordEncoder passwordEncoder) {
		String password = passwordEncoder.encode(memberDto.getPassword());
		Member member = Member.builder()
				.name(memberDto.getName())
				.email(memberDto.getEmail())
				.password(password)
				.address(memberDto.getAddress())
				.birth(memberDto.getBirth())
				.role(Role.USER)
				.build();
		return member;
		
	}
	
}
