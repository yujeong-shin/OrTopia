package com.example.ordering_lecture.address.dto;

import com.example.ordering_lecture.address.domain.Address;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddressUpdateDto {
    private String name;
    private String street;
    private String zipcode;
    private String details;

    public Address toUpdate(Address address){
        if(name !=null){
            address.updateName(name);
        }
        if(street !=null){
            address.updateStreet(street);
        }
        if(zipcode !=null){
            address.updateZipcode(zipcode);
        }
        if(details !=null){
            address.updateDetails(details);
        }
        return address;
    }
}
