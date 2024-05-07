package com.example.ordering_lecture.member.dto.Seller;

import com.example.ordering_lecture.member.domain.LikedSeller;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LikeSellerResponseDto {
    private String sellerEmail;
    private Long eventId;

    public static LikeSellerResponseDto toDto(LikedSeller likeSeller){
        return LikeSellerResponseDto.builder()
                .eventId(likeSeller.getSeller().getEventId())
                .sellerEmail(likeSeller.getSeller().getMember().getEmail())
                .build();
    }
}
