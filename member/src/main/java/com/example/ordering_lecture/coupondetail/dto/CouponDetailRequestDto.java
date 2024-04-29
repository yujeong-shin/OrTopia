package com.example.ordering_lecture.coupondetail.dto;

import com.example.ordering_lecture.coupon.domain.Coupon;
import com.example.ordering_lecture.coupondetail.domain.CouponDetail;
import com.example.ordering_lecture.member.domain.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CouponDetailRequestDto {
    @NotNull
    private String name;
    @NotNull
    private String startDate;
    @NotNull
    private String endDate;
    private int rateDiscount;
    private int fixDiscount;

    public CouponDetail toEntity(Member member){
        return CouponDetail.builder()
                .name(this.name)
                .startDate(this.startDate)
                .endDate(this.endDate)
                .rateDiscount(this.rateDiscount)
                .fixDiscount(this.fixDiscount)
                .member(member)
                .build();
    }
}