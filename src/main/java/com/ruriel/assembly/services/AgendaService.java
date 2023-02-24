package com.ruriel.assembly.services;

import com.ruriel.assembly.api.exceptions.ResourceNotFoundException;
import com.ruriel.assembly.api.exceptions.VotingHasAlreadyStartedException;
import com.ruriel.assembly.api.exceptions.VotingIsFinishedException;
import com.ruriel.assembly.entities.Agenda;
import com.ruriel.assembly.entities.VotingSession;
import com.ruriel.assembly.repositories.AgendaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

import static com.ruriel.assembly.api.exceptions.messages.AgendaMessages.AGENDA_NOT_FOUND;
import static com.ruriel.assembly.api.exceptions.messages.VotingSessionMessages.VOTING_IS_FINISHED;
import static com.ruriel.assembly.api.exceptions.messages.VotingSessionMessages.VOTING_SESSION_HAS_ALREADY_STARTED;

@Service
@RequiredArgsConstructor
public class AgendaService {
	private final AgendaRepository agendaRepository;

	private void checkVotingSession(VotingSession votingSession) {
		if (votingSession != null) {
			if (votingSession.isFinished()) {
				var message = String.format(VOTING_IS_FINISHED, votingSession.getId());
				throw new VotingIsFinishedException(message);
			}
			if (votingSession.hasStarted()) {
				var message = String.format(VOTING_SESSION_HAS_ALREADY_STARTED, votingSession.getId());
				throw new VotingHasAlreadyStartedException(message);
			}
		}
	}

	public Page<Agenda> findPage(Pageable pageable) {
		return agendaRepository.findByEnabled(true, pageable);
	}

	public Agenda create(Agenda agenda) {
		var now = LocalDateTime.now();
		agenda.setEnabled(true);
		agenda.setCreatedAt(now);
		return agendaRepository.save(agenda);
	}

	public Agenda findById(Long id) {
		return agendaRepository.findById(id).orElseThrow(() -> {
			var message = String.format(AGENDA_NOT_FOUND, id);
			return new ResourceNotFoundException(message);
		});
	}

	public Agenda update(Long id, Agenda agenda) {
		var now = LocalDateTime.now();
		var current = findById(id);
		checkVotingSession(current.getVotingSession());
		current.setDescription(agenda.getDescription());
		current.setName(agenda.getName());
		current.setUpdatedAt(now);
		current.setAssociates(agenda.getAssociates());
		return agendaRepository.save(current);

	}

	public Agenda disable(Long id) {
		var now = LocalDateTime.now();
		var current = findById(id);
		checkVotingSession(current.getVotingSession());
		current.setEnabled(false);
		current.setUpdatedAt(now);
		return agendaRepository.save(current);
	}
}
