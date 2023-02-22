package com.ruriel.assembly.api.exceptions.messages;

public class VotingSessionMessages {

    public static final String VOTING_SESSION_NOT_FOUND = "No voting session with id %d found.";

    public static final String VOTING_SESSION_HAS_ALREADY_STARTED = "Voting session with id %d has already started.";

    public static final String VOTING_IS_FINISHED = "Voting Session with id %d has already finished.";

    public static final String VOTING_HAS_NOT_STARTED = "Voting Session with id %d has not started yet.";

    private VotingSessionMessages(){}
}
