package com.example.ordering_lecture.item.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class LoveItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String email;
    @JoinColumn(name="item_id",nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Item item;
}
