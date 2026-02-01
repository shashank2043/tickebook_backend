package org.team11.tickebook.theatreservice.exception;

public class TheatreNotFoundException extends RuntimeException{
    public TheatreNotFoundException(String message) {
        super(message);
    }
}
