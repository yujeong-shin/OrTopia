package com.example.ordering_lecture.member.controller;


import com.example.ordering_lecture.common.MemberLoginReqDto;
import com.example.ordering_lecture.common.MemberLoginResDto;
import com.example.ordering_lecture.common.OrTopiaResponse;
import com.example.ordering_lecture.member.domain.Gender;
import com.example.ordering_lecture.member.domain.Seller;
import com.example.ordering_lecture.member.dto.Buyer.*;
import com.example.ordering_lecture.member.dto.Seller.LikeSellerResponseDto;
import com.example.ordering_lecture.member.dto.Seller.SellerResponseDto;
import com.example.ordering_lecture.member.service.MemberService;
import lombok.extern.slf4j.Slf4j;
import com.example.ordering_lecture.member.service.SellerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Slf4j
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
    @GetMapping("/member")
    public ResponseEntity<OrTopiaResponse> findMember(@RequestHeader("myEmail") String email){
        MemberResponseDto memberResponseDto = memberService.findIdByEmail(email);
        OrTopiaResponse orTopiaResponse = new OrTopiaResponse("read success",memberResponseDto);
        return new ResponseEntity<>(orTopiaResponse, HttpStatus.OK);
    }
    // 사용자 전체조회
    @GetMapping("/member/All")
    public ResponseEntity<OrTopiaResponse> findAllMembers(){
        List<MemberResponseDto> memberResponseDtos = memberService.findAllMembers();
        OrTopiaResponse orTopiaResponse = new OrTopiaResponse("read success",memberResponseDtos);
        return new ResponseEntity<>(orTopiaResponse, HttpStatus.OK);
    }
    // 사용자 전체조회(탈퇴X)
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
    @PostMapping("/member/likeSeller/{sellerId}")
    public ResponseEntity<OrTopiaResponse> likeSeller(@RequestHeader("myEmail") String buyerEmail, @PathVariable Long sellerId) {
        MemberLikeSellerResponseDto memberLikeSellerResponseDto = memberService.likeSeller(buyerEmail, sellerId);
        OrTopiaResponse orTopiaResponse = new OrTopiaResponse("create success", memberLikeSellerResponseDto);
        return new ResponseEntity<>(orTopiaResponse, HttpStatus.OK);
    }
    // 즐겨찾기 한 판매자 목록 조회
    @GetMapping("/member/likeSellers")
    public ResponseEntity<OrTopiaResponse> likeSellers(@RequestHeader("myEmail") String email) {
        List<SellerResponseDto> sellerResponseDtos = memberService.likeSellers(email);
        OrTopiaResponse orTopiaResponse = new OrTopiaResponse("success", sellerResponseDtos);
        return new ResponseEntity<>(orTopiaResponse, HttpStatus.OK);
    }
    // 즐겨찾기 한 구매자 목록 조회
    @GetMapping("/member/likedByBuyers")
    public ResponseEntity<OrTopiaResponse> findBuyersByLikedSeller(@RequestHeader("myEmail") String email) {
        List<MemberResponseDto> buyers = memberService.findBuyersBySellerEmail(email);
        OrTopiaResponse response = new OrTopiaResponse("read success", buyers);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    @GetMapping("/member/checkLiked/{sellerId}")
    public ResponseEntity<OrTopiaResponse> checkIfLikedBySeller(@RequestHeader("myEmail") String buyerEmail, @PathVariable Long sellerId) {
        boolean isLiked = memberService.isSellerLikedByBuyer(buyerEmail, sellerId);
        OrTopiaResponse orTopiaResponse = new OrTopiaResponse("check success", isLiked);
        return new ResponseEntity<>(orTopiaResponse, HttpStatus.OK);
    }
    // feincleint
    // item server 에서 사용
    @GetMapping("/member/search/{email}")
    public Long searchIdByEmail(@PathVariable("email") String email){
        MemberResponseDto memberResponseDto = memberService.findIdByEmail(email);
        return memberResponseDto.getId();
    }
    @DeleteMapping("/member/unlikeSeller/{sellerId}")
    public ResponseEntity<OrTopiaResponse> unlikeSeller(@RequestHeader("myEmail") String buyerEmail, @PathVariable Long sellerId) {
        memberService.unlikeSeller(buyerEmail, sellerId);
        OrTopiaResponse orTopiaResponse = new OrTopiaResponse("delete success", null);
        return new ResponseEntity<>(orTopiaResponse, HttpStatus.OK);
    }
    // 이메일을 통해서 이름을 찾는 API
    @GetMapping("/member/search/name/{email}")
    public String searchNameByEmail(@PathVariable("email") String email){
        return memberService.searchNameByEmail(email);
    }
    // 이메일을 통해서 성별을 찾는 API
    @PostMapping("/member/gender/{email}")
    public String searchGenderByEmail(@PathVariable("email") String email){
        MemberResponseDto memberResponseDto = memberService.findIdByEmail(email);
        return memberResponseDto.getGender().toString();
    }
    // 이메일을 통해서 나이를 찾는 API
    @PostMapping(value="/member/age/{email}")
    public byte searchAgeByEmail(@PathVariable(name="email") String email){
        MemberResponseDto memberResponseDto = memberService.findIdByEmail(email);
        return memberResponseDto.getAge();
    }
    // 사용자의 이메일로 id를 조회
    // item server 에서 사용
    @GetMapping("/member/search/id/{email}")
    Long findMemberIdByEmail(@PathVariable("email") String buyerEmail){
        return memberService.findIdByEmail(buyerEmail).getId();
    }

    // 구매자의 email로 자신이 팔로우하는 판매자의 이메일을 검색.
    // notice server 에서 사용
    @GetMapping("/member/search/seller/emails/{email}")
    List<LikeSellerResponseDto> searchEmailsBySellerId(@PathVariable("email") String email){
        log.info("feign 통신 성공");
        List<LikeSellerResponseDto> likeSellerResponseDtos =memberService.findSellerEmailsbyMemberEmail(email);
        memberService.updateEventId(email);
        return likeSellerResponseDtos;
    }
}
