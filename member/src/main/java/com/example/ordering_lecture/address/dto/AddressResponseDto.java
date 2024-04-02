package com.example.ordering_lecture.address.dto;

import com.example.ordering_lecture.address.domain.Address;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AddressResponseDto {
    private Long id;
    private String name;
    private String street;
    private String zipcode;
    private String details;

    public static AddressResponseDto toDto(Address address){
        return AddressResponseDto.builder()
                .id(address.getId())
                .name(address.getName())
                .street(address.getStreet())
                .zipcode(address.getZipcode())
                .details(address.getDetails())
                .build();
    }
}
