CREATE TABLE books(
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    author VARCHAR(100),
    text TEXT,
    name VARCHAR(100),
    publisher VARCHAR(100)
);

INSERT INTO books(author, text, name, publisher)
VALUES(
          'Roberto Ierusalimschy',
          'Preface. When Waldemar, Luiz, and I started the development of Lua, back in 1993, we could hardly imagine that it would spread as it did. ...',
          'Programming in LUA',
          'Teora'
      );

INSERT INTO books(author, text, name, publisher)
VALUES(
          'Jules Verne',
          'Nemaipomeniti sunt francezii astia! - Vorbiti, domnule, va ascult! ....',
          'Steaua Sudului',
          'Corint'
      );

INSERT INTO books(author, text, name, publisher)
VALUES(
          'Jules Verne',
          'Cuvant Inainte. Imaginatia copiilor - zicea un mare poet romantic spaniol - este asemenea unui cal nazdravan, iar curiozitatea lor e pintenul ce-l fugareste prin lumea celor mai indraznete proiecte.',
          'O calatorie spre centrul pamantului',
          'Polirom'
      );

INSERT INTO books(author, text, name, publisher)
VALUES(
          'Jules Verne',
          'Partea intai. Naufragiatii vazduhului. Capitolul 1. Uraganul din 1865. ...',
          'Insula Misterioasa',
          'Teora'
      );

INSERT INTO books(author, text, name, publisher)
VALUES(
          'Jules Verne',
          'Capitolul I. S-a pus un premiu pe capul unui om. Se ofera premiu de 2000 de lire ...',
          'Casa cu aburi',
          'Albatros'
      );
