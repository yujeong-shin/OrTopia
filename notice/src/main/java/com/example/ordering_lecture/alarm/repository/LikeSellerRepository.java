package com.example.ordering_lecture.alarm.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Repository
@RequiredArgsConstructor
public class LikeSellerRepository {
    private final Map<String, List<String>> sellerLikeInfo = new ConcurrentHashMap<>();

    public void save(String sellerEmail, String memberEmail) {
        if(sellerLikeInfo.containsKey(sellerEmail)){
            sellerLikeInfo.get(sellerEmail).add(memberEmail);
        }else{
            sellerLikeInfo.put(sellerEmail,new ArrayList<>());
            sellerLikeInfo.get(sellerEmail).add(memberEmail);
        }
    }

    public void deleteByEmail(String sellerEmail) {
        sellerLikeInfo.remove(sellerEmail);
    }

    public List<String> get(String sellerEmail) {
        return sellerLikeInfo.get(sellerEmail);
    }

    public boolean containKey(String sellerEmail){return sellerLikeInfo.containsKey(sellerEmail);}
}
