package com.ruriel.assembly.api.exceptions;


public class VotingHasNotStartedException extends BadRequestException{
    public VotingHasNotStartedException(String message) {
        super(message);
    }
}
