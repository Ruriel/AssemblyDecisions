package com.ruriel.assembly.api.exceptions;


public class AgendaAlreadyHasVotingSessionException extends BadRequestException {
    public AgendaAlreadyHasVotingSessionException(String message) {
        super(message);
    }
}
