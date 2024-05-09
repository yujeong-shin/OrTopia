package com.example.ordering_lecture.feign;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@org.springframework.cloud.openfeign.FeignClient(name = "ortopia-item-service", url ="http://ortopia-item-service.default.svc.cluster.local")
public interface FeignClient {
    @GetMapping(value="item/{id}/imagePath")
    String getImagePath(@PathVariable("id") Long itemId);
}
