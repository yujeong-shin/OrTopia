package com.example.ordering_lecture.member.dto;

import com.example.ordering_lecture.member.domain.Gender;
import com.example.ordering_lecture.member.domain.Member;
import com.example.ordering_lecture.member.domain.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MemberUpdateDto {
    private String email;
    private String name;
    private String password;
    private byte age;
    private Gender gender;
    private Role role;
    private String phoneNumber;

    public Member toUpdate(Member member){
        if(email !=null){
            member.updateEmail(email);
        }
        if(name != null){
            member.updateName(name);
        }
        if(password != null){
            member.updatePassword(password);
        }
        if(age != 0){
            member.updateAge(age);
        }
        if(gender != null){
            member.updateGender(gender);
        }
        if(phoneNumber != null){
            member.updatePhoneNumber(phoneNumber);
        }
        return member;
    }
}
