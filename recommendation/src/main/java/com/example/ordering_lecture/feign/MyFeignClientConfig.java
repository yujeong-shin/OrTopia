package com.example.ordering_lecture.feign;

import feign.RequestInterceptor;
import org.springframework.context.annotation.Bean;

public class MyFeignClientConfig {
    // Feign Client 요청에 토큰 설정
    @Bean
    public RequestInterceptor requestInterceptor() {
        return requestTemplate -> {
            requestTemplate.header("Authorization", "Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0ZXN0MkB0ZXN0LmNvbSIsInJvbGUiOiJCVVlFUiIsImlhdCI6MTcxMjYyNjE2MiwiZXhwIjoxNzEyNjI3OTYyfQ.qfKjQUAehjMvFUO-dToM90QO1z-w0rxCto1ErXmvHAM");
        };
    }
}
