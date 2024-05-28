package com.suhoi.demo.exception;

public class AccessPermissionDeniedException extends RuntimeException {
    public AccessPermissionDeniedException(String message) {
        super(message);
    }
}
