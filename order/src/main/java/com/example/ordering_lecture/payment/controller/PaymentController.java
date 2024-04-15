package com.example.ordering_lecture.payment.controller;

import com.example.ordering_lecture.common.OrTopiaResponse;
import com.example.ordering_lecture.payment.dto.PayApproveResDto;
import com.example.ordering_lecture.payment.dto.PayInfoDto;
import com.example.ordering_lecture.payment.dto.PayReadyResDto;
import com.example.ordering_lecture.payment.service.PaymentService;
import com.example.ordering_lecture.redis.RedisService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

@RestController
@RequestMapping("/payment")
public class PaymentController {

    private final PaymentService paymentService;
    private final RedisService redisService;

    public PaymentController(PaymentService paymentService, RedisService redisService) {
        this.paymentService = paymentService;
        this.redisService = redisService;
    }

    /** 결제 준비 redirect url 받기 --> 상품명과 가격을 같이 보내줘야함 */
    @PostMapping("/ready")
    public ResponseEntity<Object> getRedirectUrl(@RequestBody PayInfoDto payInfoDto,@RequestHeader("myEmail") String email) {
        try {
            //TODO : item 수량 체크.
            PayReadyResDto payReadyResDto =  paymentService.getRedirectUrl(email,payInfoDto);
            OrTopiaResponse orTopiaResponse = new OrTopiaResponse("success",payReadyResDto);
            return new ResponseEntity<>(orTopiaResponse,HttpStatus.OK);

        }
        catch(Exception e){
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//                    .body(new BaseResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage()));
            e.printStackTrace();
            return new ResponseEntity<>(null,HttpStatus.BAD_REQUEST);
        }
    }
    /**
     * 결제 성공 pid 를  받기 위해 request를 받고 pgToken은 rediret url에 뒤에 붙어오는걸 떼서 쓰기 위함
     * RedirectView를 통해서 결제 성공 페이지로 리다이렉트
     */
    @GetMapping("/success/{email}")
    public RedirectView afterGetRedirectUrl(@PathVariable("email")String email,
                                            @RequestParam("pg_token") String pgToken) {
        try {
            PayApproveResDto kakaoApprove = paymentService.getApprove(pgToken,email);
            // 결제가 성공하면 redis에 pgToken에 저장.
            redisService.setValues(email,pgToken);
            return new RedirectView("http://localhost:8081/order/kakao/"+pgToken);
        }
        catch(Exception e){
            e.printStackTrace();
            return new RedirectView("http://localhost:8081/");
        }
    }

    /**
     * 결제 진행 중 취소
     */
    @GetMapping("/cancel")
    public ResponseEntity<Object> cancel() {
        OrTopiaResponse orTopiaResponse = new OrTopiaResponse("사용자가 결제를 취소했습니다");
        return new ResponseEntity<>(orTopiaResponse,HttpStatus.OK);
    }

    /**
     * 결제 실패
     */
    @GetMapping("/fail")
    public ResponseEntity<Object> fail() {
        OrTopiaResponse orTopiaResponse = new OrTopiaResponse("결제 실패");
        return new ResponseEntity<>(orTopiaResponse,HttpStatus.OK);
    }
}
