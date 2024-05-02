package com.example.ordering_lecture.payment.controller;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(name = "ortopia-item-service", url ="http://ortopia-item-service.default.svc.cluster.local")
public interface ItemServiceClient {
    @PostMapping("item/search/optionDetailId/{itemId}")
    Long searchIdByOptionDetail(@PathVariable(name = "itemId") Long itemId,@RequestBody List<String> values);
    @PostMapping("item/{itemId}/itemName")
    String findNameById(@PathVariable(name="itemId") Long itemId);
}
