package com.vn.petcare.util.error;

public class PermissionException extends RuntimeException {
    public PermissionException(String message) {
        super(message);
    }
}
