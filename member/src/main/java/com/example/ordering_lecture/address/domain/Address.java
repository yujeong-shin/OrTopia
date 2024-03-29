package com.example.ordering_lecture.address.domain;

import com.example.ordering_lecture.member.domain.Member;
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

    public void updateName(String name){
        this.name = name;
    }
    public void updateStreet(String street){
        this.street = street;
    }
    public void updateZipcode(String zipcode){
        this.zipcode = zipcode;
    }
    public void updateDetails(String details){
        this.details = details;
    }
}
