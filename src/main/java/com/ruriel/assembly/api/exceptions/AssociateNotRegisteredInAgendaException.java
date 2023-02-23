package com.ruriel.assembly.api.exceptions;


public class AssociateNotRegisteredInAgendaException extends ResourceNotFoundException{
    public AssociateNotRegisteredInAgendaException(String message) {
        super(message);
    }
}
