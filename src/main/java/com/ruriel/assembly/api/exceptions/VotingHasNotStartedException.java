package com.ruriel.assembly.api.exceptions;


public class VotingHasNotStartedException extends RuntimeException{
    public VotingHasNotStartedException(String message) {
        super(message);
    }
}
