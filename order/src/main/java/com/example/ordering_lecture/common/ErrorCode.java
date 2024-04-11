package com.example.ordering_lecture.common;

import lombok.Getter;

import javax.validation.constraints.NotNull;

@Getter
public enum ErrorCode {
    REDIS_ERROR("O1","redis 저장에 실패했습니다.");

    private final String code;
    private final String message;

    ErrorCode(String code, String message) {
        this.code = code;
        this.message = message;
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
