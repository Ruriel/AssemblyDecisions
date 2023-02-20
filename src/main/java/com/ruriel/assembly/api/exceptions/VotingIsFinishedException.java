package com.ruriel.assembly.api.exceptions;


public class VotingIsFinishedException extends RuntimeException{
    public VotingIsFinishedException(String message) {
        super(message);
    }
}
