package com.ruriel.assembly.api.exceptions;

public class VotingHasAlreadyStartedException extends RuntimeException{
    public VotingHasAlreadyStartedException(String message) {
        super(message);
    }
}
