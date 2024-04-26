package com.example.ordering_lecture.item.repository;

import com.example.ordering_lecture.item.entity.Item;
import com.example.ordering_lecture.item.entity.LoveItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LoveItemRepository extends JpaRepository<LoveItem,Long> {

    Optional<LoveItem> findByItemIdAndEmail(Long ItemId,String email);
}