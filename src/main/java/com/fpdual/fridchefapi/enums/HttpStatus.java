package com.fpdual.fridchefapi.enums;

import lombok.Getter;

public enum HttpStatus {
    OK(200),
    CREATED(201),
    NO_CONTENT(204),
    NOT_MODIFIED(304),
    BAD_REQUEST(400),
    NOT_FOUND(404),
    INTERNAL_SERVER_ERROR(500);

    @Getter
    private final int statusCode;

    HttpStatus(int statusCode) {
        this.statusCode = statusCode;

    }
}