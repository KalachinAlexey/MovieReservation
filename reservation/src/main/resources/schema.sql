drop table FILMS if exists;

create table FILMS (
    ID integer primary key auto_increment,
    TITLE varchar(50),
    DESCRIPTION varchar(100) not null,
    GENRE varchar(50) not null,
    unique(TITLE)
);

create table HALLS (
    ID integer primary key,
    ROWS integer not null,
    COLUMNS integer not null
);


create table EVENTS (
    ID integer primary key auto_increment,
    HALL_ID integer not null,
    FILM_ID integer not null,
    DATE timestamp not null
);

create table PLACES (
    ID integer primary key auto_increment,
    FILM_EVENT_ID integer not null,
    ROW_NUMBER integer not null,
    COLUMN_NUMBER integer not null,
    STATUS varchar(10) not null,
    RESERVATION_ID integer,
    PRICE integer not null
);

create table RESERVATIONS (
    ID integer primary key auto_increment,
    USERNAME varchar(50),
    TOTAL_PRICE integer not null,
    STATUS varchar(10) not null
);