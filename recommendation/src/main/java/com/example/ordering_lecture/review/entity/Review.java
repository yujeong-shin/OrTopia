package com.example.ordering_lecture.review.entity;

import com.example.ordering_lecture.recommend.entity.Item;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private byte score;
    @Column(nullable = false)
    private String content;
    @Column(nullable = false)
    private Long buyerId;
    @JoinColumn(name="item_id",nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Item item;
    @Column(nullable = false)
    private Long itemId;
    @CreationTimestamp
    private LocalDateTime createdTime;
}
