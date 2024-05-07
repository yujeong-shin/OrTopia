package com.example.ordering_lecture.member.controller;

import com.example.ordering_lecture.common.OrTopiaResponse;
import com.example.ordering_lecture.member.dto.Buyer.MemberResponseDto;
import com.example.ordering_lecture.member.dto.Seller.*;
import com.example.ordering_lecture.member.service.MemberService;
import com.example.ordering_lecture.member.service.SellerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
public class SellerController {
    @Autowired
    private final SellerService sellerService;
    private final MemberService memberService;

    public SellerController(SellerService sellerService, MemberService memberService) {
        this.sellerService = sellerService;
        this.memberService = memberService;
    }

    /*
    SELLER 관련 API
    */
    // 판매자 등록 : id번 사용자의 판매자 등록 요청
    @PostMapping("/seller/{email}/create")
    public ResponseEntity<OrTopiaResponse> createSeller(@RequestHeader("myEmail") String email, @RequestBody SellerRequestDto sellerRequestDto){
        SellerResponseDto sellerResponseDto = sellerService.createSeller(email, sellerRequestDto);
        OrTopiaResponse orTopiaResponse = new OrTopiaResponse("create success", sellerResponseDto);
        return new ResponseEntity<>(orTopiaResponse, HttpStatus.CREATED);
    }
    // 판매자 상세조회
    // seller 가 로그인했을때
    @GetMapping("/seller")
    public ResponseEntity<OrTopiaResponse> findSeller(@RequestHeader("myEmail") String email){
        MemberResponseDto memberResponseDto = memberService.findIdByEmail(email);
        SellerResponseDto sellerResponseDto = sellerService.findByMemberId(memberResponseDto.getId());
        OrTopiaResponse orTopiaResponse = new OrTopiaResponse("read success", sellerResponseDto);
        return new ResponseEntity<>(orTopiaResponse, HttpStatus.OK);
    }
    // 판매자 상세조회
    // 비로그인 한 사람이 판매자 이름을 확인 할때.
    @GetMapping("/seller/noLogin/{id}")
    public ResponseEntity<OrTopiaResponse> findSeller(@PathVariable Long id){
        SellerResponseDto sellerResponseDto = sellerService.findByMemberId(id);
        OrTopiaResponse orTopiaResponse = new OrTopiaResponse("read success", sellerResponseDto);
        return new ResponseEntity<>(orTopiaResponse, HttpStatus.OK);
    }
    // 판매자 이름 조회 (Item server 에서 사용)
    // 판매자 이름을 확인 할때.
    @GetMapping("/seller/noLogin/fein/{id}")
    public SellerResponseForEventDto findSellerName(@PathVariable Long id){
        SellerResponseDto sellerResponseDto = sellerService.findByMemberId(id);
        return new SellerResponseForEventDto(sellerResponseDto.getCompanyName(), sellerResponseDto.getEventId());
    }
    // 판매자 전체조회
    @GetMapping("/seller/All")
    public ResponseEntity<OrTopiaResponse> findAllSellers(){
        List<SellerResponseDto> sellerResponseDtos = sellerService.findAllSellers();
        OrTopiaResponse orTopiaResponse = new OrTopiaResponse("read success", sellerResponseDtos);
        return new ResponseEntity<>(orTopiaResponse, HttpStatus.OK);
    }
    // 탈퇴하지 않은 판매자 전체조회
    @GetMapping("/seller/sellers")
    public ResponseEntity<OrTopiaResponse> findSellers(){
        List<SellerResponseDto> sellerResponseDtos = sellerService.findSellers();
        OrTopiaResponse orTopiaResponse = new OrTopiaResponse("read success", sellerResponseDtos);
        return new ResponseEntity<>(orTopiaResponse, HttpStatus.OK);
    }
    // 판매자 정보수정
    @PatchMapping("/seller/{id}")
    public ResponseEntity<OrTopiaResponse> updateSeller(@PathVariable Long id, @RequestBody SellerUpdateDto sellerUpdateDto){
        SellerResponseDto sellerResponseDto = sellerService.updateSeller(id, sellerUpdateDto);
        OrTopiaResponse orTopiaResponse = new OrTopiaResponse("update success", sellerResponseDto);
        return new ResponseEntity<>(orTopiaResponse, HttpStatus.OK);
    }
    // 판매자 삭제(delYN=true)
    @DeleteMapping("/seller/{id}")
    public ResponseEntity<OrTopiaResponse> deleteSeller(@PathVariable Long id){
        sellerService.deleteSeller(id);
        OrTopiaResponse orTopiaResponse = new OrTopiaResponse("delete success");
        return new ResponseEntity<>(orTopiaResponse, HttpStatus.OK);
    }
    // 판매자 Ban
    @PostMapping("/seller/ban/{id}")
    public ResponseEntity<OrTopiaResponse> banSeller(@PathVariable Long id, @RequestBody BannedSellerRequestDto bannedSellerRequestDto){
        BannedSellerResponseDto bannedSellerResponseDto = sellerService.banSeller(id, bannedSellerRequestDto);
        OrTopiaResponse orTopiaResponse = new OrTopiaResponse("ban success",bannedSellerResponseDto);
        return new ResponseEntity<>(orTopiaResponse, HttpStatus.OK);
    }
    // 판매자 Ban 풀기
    @DeleteMapping("/seller/banCancel/{id}")
    public ResponseEntity<OrTopiaResponse> banCancelSeller(@PathVariable Long id){
        sellerService.banCancelSeller(id);
        OrTopiaResponse orTopiaResponse = new OrTopiaResponse("cancel success");
        return new ResponseEntity<>(orTopiaResponse, HttpStatus.OK);
    }
    // 밴 당한 판매자 목록
    // 밴 취소된 판매자는 제외하고 리턴
    @GetMapping("/seller/bannedSellers")
    public ResponseEntity<OrTopiaResponse> bannedSellers(){
        List<BannedSellerResponseDto> bannedSellerResponseDtos = sellerService.bannedSellers();
        OrTopiaResponse orTopiaResponse = new OrTopiaResponse("read success", bannedSellerResponseDtos);
        return new ResponseEntity<>(orTopiaResponse, HttpStatus.OK);
    }
    // 판매자의 sellerId로 멤버 emails 조회.
    @GetMapping("/seller/search/emails/{sellerId}")
    List<LikeSellerResponseDto> searchEmailsBySellerId(@PathVariable("sellerId") Long sellerId){
        log.info("feinClient 통신 완료");
        return sellerService.searchEmailsBySellerId(sellerId);
    }
}
