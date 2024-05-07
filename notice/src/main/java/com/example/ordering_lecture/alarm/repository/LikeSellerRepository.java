package com.example.ordering_lecture.alarm.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Repository
@RequiredArgsConstructor
public class LikeSellerRepository {
    private final Map<String, Set<String>> sellerLikeInfo = new ConcurrentHashMap<>();

    public void save(String sellerEmail, String memberEmail) {
        if(sellerLikeInfo.containsKey(sellerEmail)){
            sellerLikeInfo.get(sellerEmail).add(memberEmail);
        }else{
            sellerLikeInfo.put(sellerEmail,new HashSet<>());
            sellerLikeInfo.get(sellerEmail).add(memberEmail);
        }
    }

    public void deleteByEmail(String buyerEmail ,String sellerEmail) {
        sellerLikeInfo.get(sellerEmail).remove(buyerEmail);
    }

    public Set<String> get(String sellerEmail) {
        return sellerLikeInfo.get(sellerEmail);
    }

    public boolean containKey(String sellerEmail){return sellerLikeInfo.containsKey(sellerEmail);}
}