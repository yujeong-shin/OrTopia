package com.example.ordering_lecture.membercoupon.dto;

import com.example.ordering_lecture.membercoupon.domain.MemberCoupon;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberCouponResponseDto {
    private Long id;
    private Long memberId;
    private Long couponId;
    private Long itemId;
    private String couponName;
    private String startDate;
    private String endDate;
    private int discount;

    public static MemberCouponResponseDto toDto(MemberCoupon memberCoupon) {
        return MemberCouponResponseDto.builder()
                .id(memberCoupon.getId())
                .memberId(memberCoupon.getMemberId())
                .couponId(memberCoupon.getCoupon().getId())
                .itemId(memberCoupon.getCoupon().getItemId())
                .couponName(memberCoupon.getCoupon().getCouponDetail().getName())
                .startDate(memberCoupon.getCoupon().getCouponDetail().getStartDate())
                .endDate(memberCoupon.getCoupon().getCouponDetail().getEndDate())
                .discount(memberCoupon.getCoupon().getCouponDetail().getRateDiscount() != 0 ?
                        memberCoupon.getCoupon().getCouponDetail().getRateDiscount() :
                        memberCoupon.getCoupon().getCouponDetail().getFixDiscount())
                .build();
    }
}