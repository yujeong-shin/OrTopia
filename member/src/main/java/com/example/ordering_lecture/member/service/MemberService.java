package com.example.ordering_lecture.member.service;

import com.example.ordering_lecture.member.domain.Member;
import com.example.ordering_lecture.member.dto.MemberRequestDto;
import com.example.ordering_lecture.member.dto.MemberResponseDto;
import com.example.ordering_lecture.member.dto.MemberUpdateDto;
import com.example.ordering_lecture.member.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import javax.transaction.Transactional;

@Service
public class MemberService {
    @Autowired
    private final PasswordEncoder passwordEncoder;
    @Autowired
    private final MemberRepository memberRepository;
    public MemberService(MemberRepository memberRepository,PasswordEncoder passwordEncoder){
        this.memberRepository = memberRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public MemberResponseDto createMember(MemberRequestDto memberRequestDto) {
        memberRequestDto.setPassword(passwordEncoder.encode(memberRequestDto.getPassword()));
        Member member = memberRequestDto.toEntity();
        memberRepository.save(member);
        return MemberResponseDto.toDto(member);
    }
    public MemberResponseDto findById(Long id) {
        //TODO : 에러 코드 추후 수정
        Member member = memberRepository.findById(id).orElseThrow();
        return MemberResponseDto.toDto(member);
    }
    @Transactional
    public MemberResponseDto updateMember(Long id, MemberUpdateDto memberUpdateDto) {
        //TODO : 에러 코드 추후 수정
        Member member = memberRepository.findById(id).orElseThrow();
        member = memberUpdateDto.toUpdate(member);
        return MemberResponseDto.toDto(member);
    }
    @Transactional
    public void deleteMember(Long id) {
        //TODO : 에러 코드 추후 수정
        Member member = memberRepository.findById(id).orElseThrow();
        member.deleteMember();
    }
}
