package com.example.ordering_lecture.member.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Seller {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String businnessNumber;

    @Column(nullable = false)
    private String companyName;

    @Column(nullable = false)
    private BusinnessType businnessType;

    @Column
    @Builder.Default
    private Long totalScore = 0L;

    @OneToOne
    @JoinColumn(nullable = false) //DB에 member_id
    private Member member;

    @OneToMany(mappedBy = "seller", cascade = CascadeType.PERSIST)
    private List<LikedSeller> likedSellers;
}
