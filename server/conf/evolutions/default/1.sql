# --- !Ups

CREATE TABLE person (
  id INT PRIMARY KEY AUTO_INCREMENT,
  firstname VARCHAR(64) NOT NULL,
  lastname VARCHAR(64) NOT NULL,
  description TEXT,
  email VARCHAR(128) NOT NULL,
  birthdate DATE NOT NULL
);

CREATE TABLE activity (
  id INT PRIMARY KEY AUTO_INCREMENT,
  name VARCHAR(64) NOT NULL
);

CREATE TABLE level (
  id INT PRIMARY KEY AUTO_INCREMENT,
  name VARCHAR(64) NOT NULL
);

CREATE TABLE location (
  id INT PRIMARY KEY AUTO_INCREMENT,
  city VARCHAR(64) NOT NULL
);

CREATE TABLE buddy (
  id INT PRIMARY KEY AUTO_INCREMENT,
  description TEXT,
  person_id INT NOT NULL,
  activity_id INT NOT NULL,
  level_id INT NOT NULL,
  location_id INT NOT NULL,
  FOREIGN KEY (person_id) REFERENCES person(id),
  FOREIGN KEY (activity_id) REFERENCES activity(id),
  FOREIGN KEY (level_id) REFERENCES level(id),
  FOREIGN KEY (location_id) REFERENCES location(id)
);

INSERT INTO person(id, firstname, lastname, description, email, birthdate) VALUES (1, 'Damien', 'Rochat', '', 'damienrochat@gmail.com', '1991-11-23' );
INSERT INTO person(id, firstname, lastname, description, email, birthdate) VALUES (2, 'Kevin', 'Ponce', 'Rien de mieux que de courir après un ballon', 'kevinponce@gmail.com', '1991-07-12' );
INSERT INTO person(id, firstname, lastname, description, email, birthdate) VALUES (3, 'Rosanne', 'Combremont', 'Le ski c\'est la vie', 'rossane@gmail.com', '1992-08-23' );
INSERT INTO person(id, firstname, lastname, description, email, birthdate) VALUES (4, 'Patrick', 'Djomo', 'Viens boxer dans la rue mec', 'patrickdjomo@gmail.com', '1997-03-12' );
INSERT INTO person(id, firstname, lastname, description, email, birthdate) VALUES (5, 'Christopher', 'Browne', 'Cherche partenaire pour grimper en extérieur', 'chrisbrown@gmail.com', '1993-05-23' );
INSERT INTO person(id, firstname, lastname, description, email, birthdate) VALUES (6, 'Sathiya', 'Kirushna', 'Allez viens faire quelques brasses, on sera bien', 'sat@gmail.com', '1995-09-03' );
INSERT INTO person(id, firstname, lastname, description, email, birthdate) VALUES (7, 'Sébastien', 'Richoz', 'Y\'en a marre de bosser', 'sebrichoz@gmail.com', '1991-10-31' );

INSERT INTO activity(id, name) VALUES (1, 'VTT');
INSERT INTO activity(id, name) VALUES (2, 'Badminton');
INSERT INTO activity(id, name) VALUES (3, 'Football');
INSERT INTO activity(id, name) VALUES (4, 'Ski');
INSERT INTO activity(id, name) VALUES (5, 'Sport de combat');
INSERT INTO activity(id, name) VALUES (6, 'Escalade');
INSERT INTO activity(id, name) VALUES (7, 'Tennis');
INSERT INTO activity(id, name) VALUES (8, 'Course à pied');
INSERT INTO activity(id, name) VALUES (9, 'Natation');
INSERT INTO activity(id, name) VALUES (10, 'Squash');

INSERT INTO level(id, name) VALUES (1, 'Débutant');
INSERT INTO level(id, name) VALUES (2, 'Intermédiaire');
INSERT INTO level(id, name) VALUES (3, 'Avancé');

INSERT INTO location(id, city) VALUES (1, 'Yverdon');
INSERT INTO location(id, city) VALUES (2, 'Verbier');
INSERT INTO location(id, city) VALUES (3, 'Ste-Croix');
INSERT INTO location(id, city) VALUES (4, 'Grandson');
INSERT INTO location(id, city) VALUES (5, 'Lausanne');
INSERT INTO location(id, city) VALUES (6, 'Yvonand');
INSERT INTO location(id, city) VALUES (7, 'Métabief');
INSERT INTO location(id, city) VALUES (8, 'Morzine');

INSERT INTO buddy(description, person_id, activity_id, level_id, location_id) VALUES ('Pour un peu de sport et beaucoup de bière', 1, 1, 3, 3);
INSERT INTO buddy(description, person_id, activity_id, level_id, location_id) VALUES ('Dispo en soirées', 1, 8, 2, 1);
INSERT INTO buddy(description, person_id, activity_id, level_id, location_id) VALUES ('De préférence le weekend', 2, 3, 3, 5);
INSERT INTO buddy(description, person_id, activity_id, level_id, location_id) VALUES ('Je peux me déplacer en voiture pour aller plus loin', 2, 8, 2, 5);
INSERT INTO buddy(description, person_id, activity_id, level_id, location_id) VALUES ('Restaurant ou pic-nic à midi, ça m\'est égal!', 3, 4, 2, 2);
INSERT INTO buddy(description, person_id, activity_id, level_id, location_id) VALUES ('Viens boxer dans la rue mec', 4, 5, 3, 1);
INSERT INTO buddy(description, person_id, activity_id, level_id, location_id) VALUES ('Viens boxer dans la rue mec', 4, 5, 3, 5);
INSERT INTO buddy(description, person_id, activity_id, level_id, location_id) VALUES ('S\'il pleut on peut aller faire du bloc', 5, 6, 3, 3);
INSERT INTO buddy(description, person_id, activity_id, level_id, location_id) VALUES ('S\'il pleut on peut aller faire du bloc', 5, 6, 3, 8);
INSERT INTO buddy(description, person_id, activity_id, level_id, location_id) VALUES ('Dispo le matin en semaine', 6, 9, 1, 1);
INSERT INTO buddy(description, person_id, activity_id, level_id, location_id) VALUES ('Pour du simple, du double ou du mixte', 7, 2, 3, 1);
INSERT INTO buddy(description, person_id, activity_id, level_id, location_id) VALUES ('Dispo les weekends. Piste ou peau de phoque', 7, 4, 3, 2);

# --- !Downs
DROP TABLE buddy;
DROP TABLE person;
DROP TABLE activity;
DROP TABLE level;
DROP TABLE location;