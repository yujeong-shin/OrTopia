package com.example.ordering_lecture.address.dto;

import com.example.ordering_lecture.address.domain.Address;
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
    @NotNull
    private Long id;
    private String name;
    private String street;
    private String zipcode;
    private String details;

    public Address toEntity(){
        Address address = Address.builder()
                .name(this.getName())
                .street(this.getStreet())
                .zipcode(this.getZipcode())
                .details(this.getDetails())
                .build();
        return address;
    }
}
