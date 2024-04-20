package com.example.ordering_lecture.member.controller;


import com.example.ordering_lecture.common.MemberLoginReqDto;
import com.example.ordering_lecture.common.MemberLoginResDto;
import com.example.ordering_lecture.common.OrTopiaResponse;
import com.example.ordering_lecture.member.domain.Seller;
import com.example.ordering_lecture.member.dto.Buyer.*;
import com.example.ordering_lecture.member.dto.Seller.SellerResponseDto;
import com.example.ordering_lecture.member.service.MemberService;
import com.example.ordering_lecture.member.service.SellerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
public class MemberController {
    @Autowired
    private final MemberService memberService;
    private final SellerService sellerService;

    public MemberController(MemberService memberService, SellerService sellerService){
        this.memberService = memberService;
        this.sellerService = sellerService;
    }
    /*
    BUYER 관련 API
     */
    // 사용자 로그인
    @PostMapping("/doLogin")
    public ResponseEntity<OrTopiaResponse> memberLogin(@Valid @RequestBody MemberLoginReqDto memberLoginReqDto) {
        MemberLoginResDto memberLoginResDto = memberService.loginService(memberLoginReqDto);
        OrTopiaResponse orTopiaResponse = new OrTopiaResponse("login success",memberLoginResDto);
        return new ResponseEntity<>(orTopiaResponse, HttpStatus.OK);
    }
    // 사용자 생성
    @PostMapping("/member/create")
    public ResponseEntity<OrTopiaResponse> createMember(@RequestBody MemberRequestDto memberRequestDto){
        MemberResponseDto memberResponseDto = memberService.createMember(memberRequestDto);
        OrTopiaResponse orTopiaResponse = new OrTopiaResponse("create success",memberResponseDto);
        return new ResponseEntity<>(orTopiaResponse, HttpStatus.CREATED);
    }
    // 사용자 상세조회
    @GetMapping("/member/{email}")
    public ResponseEntity<OrTopiaResponse> findMember(@PathVariable String email){
        MemberResponseDto memberResponseDto = memberService.findIdByEmail(email);
        OrTopiaResponse orTopiaResponse = new OrTopiaResponse("read success",memberResponseDto);
        return new ResponseEntity<>(orTopiaResponse, HttpStatus.OK);
    }
    // 판매자 전체조회
    @GetMapping("/member/All")
    public ResponseEntity<OrTopiaResponse> findAllMembers(){
        List<MemberResponseDto> memberResponseDtos = memberService.findAllMembers();
        OrTopiaResponse orTopiaResponse = new OrTopiaResponse("read success",memberResponseDtos);
        return new ResponseEntity<>(orTopiaResponse, HttpStatus.OK);
    }
    // 사용자 전체조회
    @GetMapping("/member/members")
    public ResponseEntity<OrTopiaResponse> findMembers(){
        List<MemberResponseDto> memberResponseDtos = memberService.findMembers();
        OrTopiaResponse orTopiaResponse = new OrTopiaResponse("read success",memberResponseDtos);
        return new ResponseEntity<>(orTopiaResponse, HttpStatus.OK);
    }
    // 사용자 정보수정
    @PatchMapping("/member")
    public ResponseEntity<OrTopiaResponse> updateMember(@RequestHeader("myEmail") String email, @RequestBody MemberUpdateDto memberUpdateDto){
        MemberResponseDto memberResponseDto=memberService.updateMember(email, memberUpdateDto);
        OrTopiaResponse orTopiaResponse = new OrTopiaResponse("update success",memberResponseDto);
        return new ResponseEntity<>(orTopiaResponse, HttpStatus.OK);
    }
    // 사용자 삭제(delYN=true)
    @DeleteMapping("/member/{id}")
    public ResponseEntity<OrTopiaResponse> deleteMember(@PathVariable Long id){
        memberService.deleteMember(id);
        OrTopiaResponse orTopiaResponse = new OrTopiaResponse("delete success");
        return new ResponseEntity<>(orTopiaResponse, HttpStatus.OK);
    }
    // 판매자 즐겨찾기
    @PostMapping("/member/likeSeller")
    public ResponseEntity<OrTopiaResponse> likeSeller(@RequestBody MemberLikeSellerRequestDto memberLikeSellerRequestDto){
        MemberLikeSellerResponseDto memberLikeSellerResponseDto = memberService.likeSeller(memberLikeSellerRequestDto);
        OrTopiaResponse orTopiaResponse = new OrTopiaResponse("create success",memberLikeSellerResponseDto);
        return new ResponseEntity<>(orTopiaResponse, HttpStatus.OK);
    }
    // 즐겨찾기 한 판매자 목록 조회
    @GetMapping("/member/{id}/likeSellers")
    public ResponseEntity<OrTopiaResponse> likeSellers(@PathVariable Long id){
        List<SellerResponseDto> sellerResponseDtos = memberService.likeSellers(id);
        OrTopiaResponse orTopiaResponse = new OrTopiaResponse("create success",sellerResponseDtos);
        return new ResponseEntity<>(orTopiaResponse, HttpStatus.OK);
    }
    @GetMapping("/member/search/{email}")
    public Long searchIdByEmail(@PathVariable("email") String email){
        MemberResponseDto memberResponseDto = memberService.findIdByEmail(email);
        return memberResponseDto.getId();
    }
    // 판매 내역 조회 시 사용되는 API
    // front로 넘어오는 email 값을 이용해 seller ID를 조회
    @GetMapping("/member/{email}/memberId")
    public Long findIdByMemberEmail(@PathVariable("email") String email){
        MemberResponseDto memberResponseDto = memberService.findIdByEmail(email);
        return memberResponseDto.getId();
    }
}
