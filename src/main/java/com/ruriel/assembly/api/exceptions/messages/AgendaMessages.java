package com.ruriel.assembly.api.exceptions.messages;

public class AgendaMessages {
    public static final String AGENDA_NOT_FOUND = "No agenda with id %d found.";

    public static final String AGENDA_ALREADY_HAS_VOTING_SESSION = "Agenda with id %d already has a voting session.";

    public static final String AGENDA_HAS_NO_ASSOCIATES_EXCEPTION = "Agenda with id %d has no associates to begin a voting session.";
    private AgendaMessages(){}
}
