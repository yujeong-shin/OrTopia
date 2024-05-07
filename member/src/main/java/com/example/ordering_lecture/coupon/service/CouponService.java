package com.example.ordering_lecture.coupon.service;

import com.example.ordering_lecture.common.ErrorCode;
import com.example.ordering_lecture.common.OrTopiaException;
import com.example.ordering_lecture.coupon.domain.Coupon;
import com.example.ordering_lecture.coupon.dto.CouponRequestDto;
import com.example.ordering_lecture.coupon.dto.CouponResponseDto;
import com.example.ordering_lecture.coupon.repository.CouponRepository;
import com.example.ordering_lecture.coupondetail.domain.CouponDetail;
import com.example.ordering_lecture.coupondetail.dto.CouponDetailRequestDto;
import com.example.ordering_lecture.coupondetail.repository.CouponDetailRepository;
import com.example.ordering_lecture.feign.ItemServiceClient;
import com.example.ordering_lecture.member.domain.Member;
import com.example.ordering_lecture.member.domain.Seller;
import com.example.ordering_lecture.member.repository.MemberRepository;
import com.example.ordering_lecture.member.repository.SellerRepository;
import com.example.ordering_lecture.securities.redis.RedisPublisher;
import com.example.ordering_lecture.securities.redis.RedisService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class CouponService {
    private final CouponRepository couponRepository;
    private final CouponDetailRepository couponDetailRepository;
    private final MemberRepository memberRepository;
    private final SellerRepository sellerRepository;
    private final RedisPublisher redisPublisher;
    private final ItemServiceClient itemServiceClient;
    private final RedisService redisService;

    public CouponService(CouponRepository couponRepository, CouponDetailRepository couponDetailRepository, MemberRepository memberRepository, SellerRepository sellerRepository, RedisPublisher redisPublisher, ItemServiceClient itemServiceClient, RedisService redisService) {
        this.couponRepository = couponRepository;
        this.couponDetailRepository = couponDetailRepository;
        this.memberRepository = memberRepository;
        this.sellerRepository = sellerRepository;
        this.redisPublisher = redisPublisher;
        this.itemServiceClient = itemServiceClient;
        this.redisService = redisService;
    }

    public List<CouponResponseDto> createCoupon(CouponRequestDto couponRequestDto) throws OrTopiaException {
        CouponDetail couponDetail = couponDetailRepository.findById(couponRequestDto.getCouponDetailId())
                .orElseThrow(() -> new OrTopiaException(ErrorCode.COUPON_CREATION_FAILED));
        List<Coupon> coupons = couponRequestDto.getItemId().stream()
                .map(itemId -> Coupon.builder()
                        .itemId(itemId)
                        .couponDetail(couponDetail)
                        .build())
                .collect(Collectors.toList());
        couponRepository.saveAll(coupons);
        return coupons.stream()
                .map(coupon -> CouponResponseDto.toDto(coupon, couponDetail))
                .collect(Collectors.toList());
    }
    public List<CouponResponseDto> getCoupon(Long itemId) throws OrTopiaException {
        List<Coupon> coupons = couponRepository.findByItemId(itemId);
        if (coupons.isEmpty()) {
            throw new OrTopiaException(ErrorCode.COUPON_NOT_FOUND);
        }
        return coupons.stream()
                .map(coupon -> CouponResponseDto.toDto(coupon, coupon.getCouponDetail()))
                .collect(Collectors.toList());
    }
    @Transactional
    public void createMessage(String email, List<CouponResponseDto> couponResponseDtos) {
        LocalDate currentDate = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd");
        String nowDate = currentDate.format(formatter);
        Member sellerMember = memberRepository.findByEmail(email).orElseThrow(
                ()-> new OrTopiaException(ErrorCode.NOT_FOUND_MEMBER)
                );
        Seller seller = sellerRepository.findByMemberId(sellerMember.getId()).orElseThrow(
                ()-> new OrTopiaException(ErrorCode.NOT_FOUND_SELLER)
        );
        String companyName = seller.getCompanyName();
        log.info(email+"채널에 메시지를 보냅니다.");
        ChannelTopic channel = new ChannelTopic(email);
        for(CouponResponseDto couponResponseDto : couponResponseDtos) {
            String itemName = itemServiceClient.findNameById(couponResponseDto.getItemId());
            String message = seller.getEventId()+"_"+nowDate + "_" + email + "_" + companyName + "이 님이 " + itemName + "에 쿠폰을 등록했어요!" + "_" + "itemId:" + couponResponseDto.getItemId();
            seller.updateEventId();
            redisPublisher.publish(channel, message);
            // 알람메시지 캐싱
            redisService.setValues(email,message);
            log.info(email + "채널에 성공적으로 알람을 발송 했습니다.");
        }
    }
}