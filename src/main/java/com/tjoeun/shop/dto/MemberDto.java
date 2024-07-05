package com.tjoeun.shop.dto;

import java.time.LocalDate;

import org.hibernate.validator.constraints.Length;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemberDto {
	
	@NotBlank(message="이름을 입력해주세요.")
	private String name;
	
	@NotBlank(message="이메일을 입력해주세요.")
	@Email
	private String email;
	
	@NotBlank(message="비밀번호를 입력해주세요.")
	@Length(min=6, max=20, message="비밀번호는 6-20글자 사이로 입력해주세요.")
	private String password;
	
	@NotBlank(message = "비밀번호를 다시 입력해주세요.")
    private String confirmPassword;
	
	@NotEmpty(message="주소를 입력해주세요.")
	private String address;
	
	@NotBlank(message="생일을 입력해주세요.")
	private  LocalDate birth;

}
