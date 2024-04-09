package com.example.ordering_lecture.address.dto;

import com.example.ordering_lecture.address.domain.Address;
import com.example.ordering_lecture.member.domain.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AddressRequestDto {
    private String name;
    private String sido;
    private String sigungu;
    private String bname;
    private String roadAddress;
    private String zonecode;
    private String detail;

    public Address toEntity(Member member){
        return Address.builder()
                .name(this.name)
                .sido(this.sido)
                .sigungu(this.sigungu)
                .bname(this.bname)
                .roadAddress(this.roadAddress)
                .zonecode(this.zonecode)
                .detail(this.detail)
                .member(member)
                .build();
    }
}