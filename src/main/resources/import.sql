INSERT INTO AGENDA(name, description, enabled, created_at) VALUES ('Agenda 1', 'First agenda of the week.', true, current_timestamp);
INSERT INTO AGENDA(name, description, enabled, created_at) VALUES ('Agenda 2', 'Second agenda of the week.', true, current_timestamp);
INSERT INTO AGENDA(name, description, enabled, created_at) VALUES ('Agenda 3', 'Third agenda of the week.', true, current_timestamp);

INSERT INTO ASSOCIATE(name, document, enabled, created_at) VALUES ('Fulano de tal', '12345678901', true, current_timestamp);
INSERT INTO ASSOCIATE(name, document, enabled, created_at) VALUES ('Siclano', '45678945612', true, current_timestamp);

INSERT INTO ASSOCIATES_AGENDAS(agenda_id, associate_id) VALUES (1, 1);
INSERT INTO ASSOCIATES_AGENDAS(agenda_id, associate_id) VALUES (2, 1), (2,2);

INSERT INTO VOTING_SESSION(id, starts_at, ends_at, created_at) VALUES(1, current_timestamp, TIMESTAMPADD('MINUTE', 10, current_timestamp), current_timestamp);
UPDATE AGENDA SET voting_session_id = 1 WHERE id = 1;
--INSERT INTO VOTING_SESSION(id, starts_at, ends_at, agenda_id, created_at) VALUES(2, current_timestamp, TIMESTAMPADD('MINUTE', 1, current_timestamp), 1, current_timestamp);
--INSERT INTO VOTING_SESSION(id, starts_at, ends_at, agenda_id, created_at) VALUES(3, TIMESTAMPADD('MINUTE', 10, current_timestamp), TIMESTAMPADD('MINUTE', 11, current_timestamp), 1, current_timestamp);