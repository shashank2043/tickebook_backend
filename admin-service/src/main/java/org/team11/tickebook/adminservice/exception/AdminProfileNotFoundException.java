package org.team11.tickebook.adminservice.exception;

public class AdminProfileNotFoundException extends RuntimeException {

    public AdminProfileNotFoundException(String message) {
        super(message);
    }
}