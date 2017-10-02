show tables;
create database web;
delete table 
select distinct * from users;
delete from  luo_point;
select * from luo_point;
select * from luo_data group by pointnum;
select * from luo_data ;

create table if not exists users(
	id int primary key auto_increment,
	username varchar(20) unique not null,
	password varchar(20) not null,
	regdate datetime);
	
create table if not exists parameters(
	id int primary key auto_increment,
	username varchar(20),
	pointnum varchar(6),
	point varchar(16),
	air_t float, 
	air_h int(4),
	ill int(6),
	co2 int(6),
	soil_t float, 
	soil_h int(4),
	voltage float,
	date datetime);
	
create table if not exists weathers(
	id int primary key auto_increment,
	pointNum varchar(6),
	temperature float,
	humidity  int(4),
	windSpeed float,
	windDir float, 
    solarRadiation float,
	batteryV float,
	solarV float,
	date datetime);
	
create table if not exists points(
	id int primary key auto_increment,
	pointnum varchar(6),
	fpoint varchar(16),
	point varchar(16),
	date datetime);
	
create table if not exists systems(
	id int primary key auto_increment,
	username varchar(20),
	type varchar(10),
	port varchar(6),
	botelv varchar(6),
	date datetime);
	
create table if not exists topologys(
	id int primary key auto_increment,
	point varchar(16),
	fpoint varchar(16),
	quality int(4),
	date datetime);
	
create table if not exists devices(
	id int primary key auto_increment,
	name varchar(16),
	address varchar(16),
	xposition int(6),
	yposition int(6),
	zposition int(6),
	date datetime);
	
create table if not exists charts(
	id int primary key auto_increment,
	username varchar(20),
	name varchar(16),
	point varchar(16),
	datatype varchar(20),
	date datetime);
	
create table if not exists alerts(
	id int primary key auto_increment,
	name varchar(20),
	point varchar(16),
	datatype varchar(20),
	min int(8),
	max int(8),
	date datetime);
	
create table if not exists alertinfos(
	id int primary key auto_increment,
	name varchar(20),
	point varchar(16),
	reason varchar(50),
	date datetime);
	
	
	