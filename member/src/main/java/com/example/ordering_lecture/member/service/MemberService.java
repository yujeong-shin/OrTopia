package com.example.ordering_lecture.member.service;

import com.example.ordering_lecture.common.ErrorCode;
import com.example.ordering_lecture.common.OrTopiaException;
import com.example.ordering_lecture.member.domain.LikedSeller;
import com.example.ordering_lecture.member.domain.Member;
import com.example.ordering_lecture.member.domain.Seller;
import com.example.ordering_lecture.member.dto.Buyer.*;
import com.example.ordering_lecture.member.dto.Seller.SellerResponseDto;
import com.example.ordering_lecture.member.repository.LikedSellerRepository;
import com.example.ordering_lecture.member.repository.MemberRepository;
import com.example.ordering_lecture.member.repository.SellerRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MemberService {
    private final PasswordEncoder passwordEncoder;
    private final MemberRepository memberRepository;
    private final SellerRepository sellerRepository;
    private final LikedSellerRepository likedSellerRepository;
    public MemberService(MemberRepository memberRepository, PasswordEncoder passwordEncoder, SellerRepository sellerRepository, LikedSellerRepository likedSellerRepository){
        this.memberRepository = memberRepository;
        this.passwordEncoder = passwordEncoder;
        this.sellerRepository = sellerRepository;
        this.likedSellerRepository = likedSellerRepository;
    }
    public MemberResponseDto createMember(MemberRequestDto memberRequestDto) throws OrTopiaException{
        memberRequestDto.setPassword(passwordEncoder.encode(memberRequestDto.getPassword()));
        Member member = memberRequestDto.toEntity();
        memberRepository.save(member);
        return MemberResponseDto.toDto(member);
    }
    public MemberResponseDto findById(Long id) throws OrTopiaException {
        //TODO : 에러 코드 추후 수정
        Member member = memberRepository.findById(id).orElseThrow(
                ()-> new OrTopiaException(ErrorCode.NOT_FOUND_MEMBER)
                );
        return MemberResponseDto.toDto(member);
    }
    public List<MemberResponseDto> findAllMembers() throws OrTopiaException {
        List<MemberResponseDto> memberResponseDtos = memberRepository.findAll()
                .stream()
                .map(MemberResponseDto::toDto)
                .collect(Collectors.toList());
        if(memberResponseDtos.isEmpty()){
            throw new OrTopiaException(ErrorCode.NOT_FOUND_MEMBERS);
        }
        return memberResponseDtos;
    }
    public List<MemberResponseDto> findMembers() throws OrTopiaException{
        List<MemberResponseDto> memberResponseDtos = memberRepository.findByDelYNFalse()
                .stream()
                .map(MemberResponseDto::toDto)
                .collect(Collectors.toList());
        if(memberResponseDtos.isEmpty()){
            throw new OrTopiaException(ErrorCode.NOT_FOUND_MEMBERS);
        }
        return memberResponseDtos;
    }
    @Transactional
    public MemberResponseDto updateMember(Long id, MemberUpdateDto memberUpdateDto) throws OrTopiaException{
        Member member = memberRepository.findById(id).orElseThrow(
                ()-> new OrTopiaException(ErrorCode.NOT_FOUND_MEMBER)
                );
        member.updateMember(memberUpdateDto);
        return MemberResponseDto.toDto(member);
    }
    @Transactional
    public void deleteMember(Long id) throws OrTopiaException{
        Member member = memberRepository.findById(id).orElseThrow(
                ()-> new OrTopiaException(ErrorCode.NOT_FOUND_MEMBER)
                );
        member.deleteMember();
        Seller seller = sellerRepository.findById(id).orElseThrow(
                ()-> new OrTopiaException(ErrorCode.NOT_FOUND_SELLER)
                );
        seller.deleteSeller();
    }
    @Transactional
    public MemberLikeSellerResponseDto likeSeller(MemberLikeSellerRequestDto memberLikeSellerRequestDto)  throws OrTopiaException  {
        //TODO : 에러 코드 추후 수정
        // memberID와 sellerID가 같다면 에러 처리
        Member buyer = memberRepository.findById(memberLikeSellerRequestDto.getBuyerID()).orElseThrow(
                ()-> new OrTopiaException(ErrorCode.NOT_FOUND_MEMBER)
                );
        Seller seller = sellerRepository.findById(memberLikeSellerRequestDto.getSellerID()).orElseThrow(
                ()-> new OrTopiaException(ErrorCode.NOT_FOUND_SELLER)
                );
        LikedSeller likedSeller = memberLikeSellerRequestDto.toEntity(buyer, seller);
        likedSellerRepository.save(likedSeller);
        return MemberLikeSellerResponseDto.toDto(likedSeller);
    }

    public List<SellerResponseDto> likeSellers(Long id) throws OrTopiaException {
        List<SellerResponseDto> sellerResponseDtos = likedSellerRepository.findAllByBuyerId(id)
                .stream()
                .map(likedSeller -> SellerResponseDto.toDto(likedSeller.getSeller()))
                .collect(Collectors.toList());
        if(sellerResponseDtos.isEmpty()){
            throw new OrTopiaException(ErrorCode.NOT_FOUND_SELLERS);
        }
        return sellerResponseDtos;
    }
}
