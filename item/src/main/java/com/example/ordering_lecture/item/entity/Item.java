package com.example.ordering_lecture.item.entity;

import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private int stock;
    @Column(nullable = false)
    private int price;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Category category;
    @Column(nullable = false)
    private String detail;
    @Column(nullable = false)
    private String imagePath;
    @Column
    private int minimumStock;
    @Column(nullable = false)
    private boolean delYN;
    @Column(nullable = false)
    private boolean isBaned;
    @Column(nullable = false)
    private String sellerEmail;
    @CreationTimestamp
    public LocalDateTime createdTime;
}
