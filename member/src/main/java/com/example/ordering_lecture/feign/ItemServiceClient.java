package com.example.ordering_lecture.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "ortopia-item-service", url ="http://ortopia-item-service.default.svc.cluster.local")
public interface ItemServiceClient {

    @PostMapping("/{itemId}/itemName")
    public String findNameById(@PathVariable Long itemId);
}
