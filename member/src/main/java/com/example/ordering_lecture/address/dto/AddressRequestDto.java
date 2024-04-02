package com.example.ordering_lecture.address.dto;

import com.example.ordering_lecture.address.domain.Address;
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
public class AddressRequestDto {
    private String name;
    private String street;
    private String zipcode;
    private String details;

    public Address toEntity(Member member){
        Address address = Address.builder()
                .member(member)
                .name(this.getName())
                .street(this.getStreet())
                .zipcode(this.getZipcode())
                .details(this.getDetails())
                .build();
        return address;
    }
}
