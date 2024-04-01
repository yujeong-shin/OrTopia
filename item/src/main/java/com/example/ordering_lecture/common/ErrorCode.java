package com.example.ordering_lecture.common;

import lombok.Getter;

@Getter
public enum ErrorCode {
    WRONG_ITEM_INFORMATION("I1","아이템의 정보를 다시 입력해주세요."),
    NOT_FOUND_ITEM("I2","아이템을 찾을 수 없습니다"),
    EMPTY_ITEMS("I3","아이템 목록을 찾을 수 없습니다"),
    EMPTY_ITEM_NAME("I4","아이템의 이름을 입력해주세요"),
    EMPTY_ITEM_STOCK("I5","아이템의 재고를 입력해주세요"),
    EMPTY_ITEM_PRICE("I6","아이템의 가격을 입력해주세요"),
    EMPTY_ITEM_CATEGORY("I7","아이템의 카테고리를 입력해주세요"),
    EMPTY_ITEM_DETAIL("I8","아이템의 설명을 입력해주세요"),
    EMPTY_ITEM_SELLER("I9","아이템의 판매자 이메일 입력해주세요");
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
