package com.company.enroller.controllers;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.company.enroller.model.Participant;
import com.company.enroller.persistence.ParticipantService;

@RestController
@RequestMapping("/participants")
public class ParticipantRestController {

	@Autowired
	ParticipantService participantService;

	@RequestMapping(value = "", method = RequestMethod.GET)
	public ResponseEntity<?> getParticipants(@RequestParam(required = false) String sortBy, @RequestParam(required = false) String sortOrder)		 {

		Collection<Participant> participants = participantService.getAll(sortBy, sortOrder);
		return new ResponseEntity<Collection<Participant>>(participants, HttpStatus.OK);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public ResponseEntity<?> getParticipant(@PathVariable("id") String login) {
		Participant participant = participantService.findByLogin(login);
		if (participant == null) {
			return new ResponseEntity(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<Participant>(participant, HttpStatus.OK);
	}

	@RequestMapping(value = "", method = RequestMethod.POST)
	public ResponseEntity<?> registerParticipant(@RequestBody Participant participant) {

		Participant foundParticipant = participantService.findByLogin(participant.getLogin());
		if (foundParticipant != null) {
			return new ResponseEntity("Unable to create. A participant with login " + participant.getLogin() + " already exist.", HttpStatus.CONFLICT);
		}

		Participant participant1 = participantService.registerParticipant(participant);
		return new ResponseEntity<Participant>(participant1, HttpStatus.CREATED);

	}

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
		public ResponseEntity<?> deleteParticipant(@PathVariable("id") String login) {
			Participant participant = participantService.findByLogin(login);
			if (participant == null) {
				return new ResponseEntity("Unable to delete. A participant with login " + participant.getLogin() + " does not exist.", HttpStatus.CONFLICT);
			}
			Participant participant1 = participantService.removeByLogin(login);
			return new ResponseEntity<Participant>(participant1, HttpStatus.OK);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	public ResponseEntity<?> updateParticipant(@PathVariable("id") String login, @RequestBody Participant participant) {
		Participant foundParticipant = participantService.findByLogin(login);
		if (foundParticipant == null) {
			return new ResponseEntity("Unable to find. A participant with login " + participant.getLogin() + " does not exist.", HttpStatus.CONFLICT);
		}
		Participant foundParticipant1 = participantService.updateByLogin(participant,foundParticipant);
		return new ResponseEntity<Participant>(foundParticipant1, HttpStatus.OK);

	}


}
