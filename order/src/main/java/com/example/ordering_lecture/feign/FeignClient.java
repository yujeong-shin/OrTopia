package com.example.ordering_lecture.feign;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@org.springframework.cloud.openfeign.FeignClient(name = "ortopia-member-service", url ="http://ortopia-member-service.default.svc.cluster.local")
public interface FeignClient {
    @GetMapping(value="/member/{email}/memberId")
    Long findIdByMemberEmail(@PathVariable("email") String email);
}
