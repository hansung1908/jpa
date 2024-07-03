create database jpa;
use jpa;

CREATE TABLE user (
    email VARCHAR(255) PRIMARY KEY,
    name VARCHAR(255),
    create_date DATETIME
);

create table hotel_info (
	hotel_id varchar(255) primary key,
    nm varchar(255),
    year int,
    grade varchar(2),
    created datetime,
    modified datetime
);

create table review (
	review_id int primary key,
    hotel_id varchar(255),
    mark int,
    writer_name varchar(255),
	`comment` text,
    created datetime
);

ALTER TABLE review MODIFY review_id INT AUTO_INCREMENT;

create table access_log(
	id int primary key,
    path varchar(255),
    accessed datetime
);

create table id_seq (
    entity varchar(255) primary key,
    nextval bigint
);

create table hotel2_info (
  hotel_id varchar(255) primary key,
  nm varchar(255),
  year int,
  grade varchar(2),
  addr1 varchar(255),
  addr2 varchar(255),
  zipcode varchar(255),
  created datetime,
  modified datetime
);

create table employee (
  id varchar(255) primary key,
  addr1 varchar(255),
  addr2 varchar(255),
  zipcode varchar(255),
  waddr1 varchar(255),
  waddr2 varchar(255),
  wzipcode varchar(255)
);

create table writer (
  id int auto_increment primary key,
  name varchar(255)
);

create table writer_intro (
  writer_id int primary key,
  content_type varchar(255),
  content text
);

create table writer_address (
  writer_id int primary key,
  addr1 varchar(255),
  addr2 varchar(255),
  zipcode varchar(255)
);

create table role (
  id varchar(255) primary key,
  name varchar(255)
);

create table role_perm (
  role_id varchar(255),
  perm varchar(255),
  grantor varchar(255),
  primary key (role_id, perm)
);

create table question (
  id varchar(255) primary key,
  text varchar(255)
);

create table question_choice (
  question_id varchar(255),
  idx int,
  text varchar(255),
  input boolean,
  primary key (question_id, text)
);

create table doc (
  id varchar(255) primary key,
  title varchar(255),
  content varchar(255)
);

create table doc_prop (
  doc_id varchar(255),
  name varchar(255),
  value varchar(255),
  enabled boolean,
  primary key (doc_id, name)
);

create table membership_card (
  card_no varchar(255) primary key,
  user_email varchar(255),
  expiry_date date,
  enabled boolean
);

create table best_pick (
  user_email varchar(255) primary key,
  title varchar(255)
);