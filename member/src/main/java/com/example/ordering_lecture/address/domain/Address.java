package com.example.ordering_lecture.address.domain;

import com.example.ordering_lecture.address.dto.AddressUpdateDto;
import com.example.ordering_lecture.member.domain.Member;
import com.example.ordering_lecture.member.dto.Buyer.MemberUpdateDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private String street;
    @Column(nullable = false)
    private String zipcode;
    @Column(nullable = false)
    private String details;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private Member member;

    public void updateAddress(AddressUpdateDto addressUpdateDto) {
        if (addressUpdateDto.getName() != null && !addressUpdateDto.getName().equals(this.name)) {
            this.name = addressUpdateDto.getName();
        }
        if (addressUpdateDto.getStreet() != null && !addressUpdateDto.getStreet().equals(this.street)) {
            this.street = addressUpdateDto.getStreet();
        }
        if (addressUpdateDto.getZipcode() != null && !addressUpdateDto.getZipcode().equals(this.zipcode)) {
            this.zipcode = addressUpdateDto.getZipcode();
        }
        if (addressUpdateDto.getDetails() != null && !addressUpdateDto.getDetails().equals(this.details)) {
            this.details = addressUpdateDto.getDetails();
        }
    }
}
