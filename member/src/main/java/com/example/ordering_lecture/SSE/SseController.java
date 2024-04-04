package com.example.ordering_lecture.SSE;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
public class SseController {
    private final SseEmittersService sseEmittersService;
    @Autowired
    public SseController(SseEmittersService sseEmittersService) {
        this.sseEmittersService = sseEmittersService;
    }
    @GetMapping(value = "/connect", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public ResponseEntity<SseEmitter> connect(@RequestParam String email) {
        SseEmitter emitter = new SseEmitter(30 * 60 * 1000L); // 30분
        sseEmittersService.add(email, emitter);
        return ResponseEntity.ok(emitter);
    }
}