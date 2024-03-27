package com.example.ordering_lecture.member.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false,unique = true)
    private String email;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String password;

    @OneToMany(mappedBy = "member")
    private List<Address> addresses;

    @Column(nullable = false)
    private byte age;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Gender gender;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private Role role = Role.BUYER;

    @Column(nullable = false)
    private String phoneNumber;

    @Column(nullable = false)
    @Builder.Default
    private boolean delYN = false;

    @CreationTimestamp
    private LocalDateTime createdTime;

    @Column
    private String lastLoginTime;

    @OneToOne(mappedBy = "member", cascade = CascadeType.PERSIST)
    private Seller seller;

    @OneToMany(mappedBy = "buyer", cascade = CascadeType.PERSIST)
    private List<LikedSeller> likedSellers;

    public void updateEmail(String email){
        this.email = email;
    }
    public void updateName(String name){
        this.name = name;
    }
    public void updatePassword(String password){
        this.password = password;
    }
    public void updateAge(byte age){
        this.age = age;
    }
    public void updateGender(Gender gender){
        this.gender = gender;
    }
    public void updateRole(Role role){
        this.role = role;
    }
    public void updatePhoneNumber(String phoneNumber){
        this.phoneNumber = phoneNumber;
    }
    public void updateCreatedTime(LocalDateTime createdTime){
        this.createdTime = createdTime;
    }
    public void updateLastLoginTime(String lastLoginTime){
        this.lastLoginTime = lastLoginTime;
    }
    public void deleteMember(){
        this.delYN = true;
    }
}