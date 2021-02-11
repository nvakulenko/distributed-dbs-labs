drop database if exists fly_booking;
create database fly_booking;
\connect fly_booking;

create table fly_booking.booking (
    booking_id serial primary key,
    client_name varchar(200),
    fly_number varchar(10),
    from_airport varchar(3),
    to_airport varchar(3),
    departure_date date
);


drop database if exists hotel_booking;
create database hotel_booking;
\connect hotel_booking;

create table hotel_booking.booking (
  booking_id serial primary key,
  client_name varchar(200),
  hotel_name varchar(200),
  arrival_date date,
  departure_date date
);


drop database if exists account;
create database account;
\connect account;

create table account.account (
  account_id serial primary key,
  client_name varchar(200),
  amount decimal CHECK (amount > 0)
);