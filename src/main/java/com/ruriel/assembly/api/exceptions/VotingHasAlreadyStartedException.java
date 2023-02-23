package com.ruriel.assembly.api.exceptions;

public class VotingHasAlreadyStartedException extends BadRequestException{
    public VotingHasAlreadyStartedException(String message) {
        super(message);
    }
}
