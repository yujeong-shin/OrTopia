package com.example.ordering_lecture.member.dto;

import com.example.ordering_lecture.member.domain.Address;
import com.example.ordering_lecture.member.domain.Member;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class MemberResponseDto {
    private Long id;
    private String name;
    private String email;
    private String city;
    private String street;
    private String zipcode;
    private String phoneNumber;

    public static MemberResponseDto toDto(Member member) {
        MemberResponseDtoBuilder builder = MemberResponseDto.builder();
        builder.id(member.getId());
        builder.name(member.getName());
        builder.email(member.getEmail());
        List<Address> addresses = member.getAddresses();
        for(Address address : addresses) {
            if (address != null){
                builder.city(address.getName());
                builder.street(address.getStreet());
                builder.zipcode(address.getZipcode());
                builder.city(address.getDetails());
            }
        }
        builder.phoneNumber(member.getPhoneNumber());
        return builder.build();
    }
}
