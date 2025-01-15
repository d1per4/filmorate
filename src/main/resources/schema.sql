drop table if exists mpa cascade;
drop table if exists films cascade;
drop table if exists users cascade;
drop table if exists likes cascade;
drop table if exists genres cascade;
drop table if exists film_genres cascade;
drop table if exists friends cascade;


create table if not exists mpa
(
    id       serial primary key,
    name_mpa varchar(50) not null
);

create table if not exists films
(
    id           serial primary key,
    name         varchar(255) not null,
    description  varchar(200),
    release_date date,
    duration     integer,
    mpa_id       integer      not null references mpa
);


create table if not exists users
(
    id       serial primary key,
    email    varchar(50),
    login    varchar(100) not null,
    name     varchar(50),
    birthday date
);


create table if not exists likes
(
    film_id integer not null references films,
    user_id integer not null references users,
    primary key (film_id, user_id)
);


create table if not exists genres
(
    id         serial primary key,
    name_genre varchar(50) not null
);


create table if not exists film_genres
(
    genre_id integer not null references genres,
    film_id  integer not null references films,
    primary key (genre_id, film_id)
);


create table if not exists friends
(
    user_id   integer not null references users,
    friend_id integer not null references users,
    primary key (user_id, friend_id)
);


