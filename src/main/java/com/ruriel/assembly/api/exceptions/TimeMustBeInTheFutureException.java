package com.ruriel.assembly.api.exceptions;

public class TimeMustBeInTheFutureException extends BadRequestException{
    public TimeMustBeInTheFutureException(String message){ super(message);}
}
