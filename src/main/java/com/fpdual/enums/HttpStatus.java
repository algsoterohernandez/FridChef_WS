package com.fpdual.enums;

import lombok.Getter;

/**
 * Enumeración de códigos de estado HTTP.
 */
public enum HttpStatus {
    OK(200),
    CREATED(201),
    NO_CONTENT(204),
    NOT_MODIFIED(304),
    BAD_REQUEST(400),
    NOT_FOUND(404),
    INTERNAL_SERVER_ERROR(500);

    /**
     * Código de estado HTTP.
     */
    @Getter
    private final int statusCode;

    /**
     * Constructor de HttpStatus.
     * @param statusCode El código de estado HTTP asociado al enumerador.
     */
    HttpStatus(int statusCode) {
        this.statusCode = statusCode;

    }
}