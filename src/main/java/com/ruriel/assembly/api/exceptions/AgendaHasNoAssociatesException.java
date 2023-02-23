package com.ruriel.assembly.api.exceptions;

public class AgendaHasNoAssociatesException extends BadRequestException{
    public AgendaHasNoAssociatesException(String message) {
        super(message);
    }
}
