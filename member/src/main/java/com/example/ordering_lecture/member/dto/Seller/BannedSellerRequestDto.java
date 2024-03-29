package com.example.ordering_lecture.member.dto.Seller;

import com.example.ordering_lecture.member.domain.BannedSeller;
import com.example.ordering_lecture.member.domain.Seller;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BannedSellerRequestDto {
    private String details;
    private String startTime;
    private String endTime;
    public BannedSeller toEntity(Seller seller, BannedSellerRequestDto bannedSellerRequestDto){
        BannedSeller bannedSeller = BannedSeller.builder()
                .seller(seller)
                .details(bannedSellerRequestDto.getDetails())
                .startTime(bannedSellerRequestDto.getStartTime())
                .endTime(bannedSellerRequestDto.getEndTime())
                .build();
        return bannedSeller;
    }
}
