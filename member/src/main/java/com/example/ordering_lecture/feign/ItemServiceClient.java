package com.example.ordering_lecture.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "ortopia-item-service", url ="http://ortopia-item-service.default.svc.cluster.local")
public interface ItemServiceClient {

    @PostMapping("item/{itemId}/itemName")
    String findNameById(@PathVariable(name="itemId") Long itemId);
}
