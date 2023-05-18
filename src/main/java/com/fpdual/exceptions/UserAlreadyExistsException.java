package com.fpdual.exceptions;

public class UserAlreadyExistsException extends FridChefException{
    public UserAlreadyExistsException(String message) {
        super(message);
    }
}