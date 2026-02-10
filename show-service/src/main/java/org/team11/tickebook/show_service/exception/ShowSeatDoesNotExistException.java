package org.team11.tickebook.show_service.exception;

public class ShowSeatDoesNotExistException extends RuntimeException{
    public ShowSeatDoesNotExistException(String message) {
        super(message);
    }
}
