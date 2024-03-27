package com.example.ordering_lecture.member.controller;

import com.example.ordering_lecture.member.dto.MemberRequestDto;
import com.example.ordering_lecture.member.dto.MemberUpdateDto;
import com.example.ordering_lecture.member.service.MemberService;
import com.example.ordering_lecture.securities.JwtTokenProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
public class MemberController {
    @Autowired
    private final JwtTokenProvider jwtTokenProvider;
    @Autowired
    private final MemberService memberService;

    public MemberController(JwtTokenProvider jwtTokenProvider, MemberService memberService){
        this.jwtTokenProvider = jwtTokenProvider;
        this.memberService = memberService;
    }

    @PostMapping("/member/create")
    public Object createMember(@RequestBody MemberRequestDto memberRequestDto){
        return memberService.createMember(memberRequestDto);
    }
    @GetMapping("/member/{id}")
    public Object findMember(@PathVariable Long id){
        return memberService.findById(id);
    }
    @PatchMapping("/member/{id}")
    public Object updateMember(@PathVariable Long id, @RequestBody MemberUpdateDto memberUpdateDto){
        return memberService.updateMember(id, memberUpdateDto);
    }
    @DeleteMapping("/member/{id}")
    public void deleteMember(@PathVariable Long id){
        memberService.deleteMember(id);
    }
}
