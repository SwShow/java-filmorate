-- Заполнение данными таблиц genres и mpa_rating

DELETE FROM film_genre_join;
DELETE FROM films;
DELETE FROM friends;
DELETE FROM rating;
DELETE FROM users;
DELETE FROM mpa_rating;
DELETE FROM genres;

INSERT INTO mpa_rating (id_rate,name_rate) VALUES (1,'G');
INSERT INTO mpa_rating (id_rate,name_rate) VALUES (2,'PG');
INSERT INTO mpa_rating (id_rate,name_rate) VALUES (3,'PG-13');
INSERT INTO mpa_rating (id_rate,name_rate) VALUES (4,'R');
INSERT INTO mpa_rating (id_rate,name_rate) VALUES (5,'NC-17');
INSERT INTO genres (id_genre,name_genre) VALUES (1,'Комедия');
INSERT INTO genres (id_genre,name_genre) VALUES (2,'Драма');
INSERT INTO genres (id_genre,name_genre) VALUES (3,'Мультфильм');
INSERT INTO genres (id_genre,name_genre) VALUES (4,'Триллер');
INSERT INTO genres (id_genre,name_genre) VALUES (5,'Документальный');
INSERT INTO genres (id_genre,name_genre) VALUES (6,'Боевик');




