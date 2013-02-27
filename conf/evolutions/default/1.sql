# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table pdf_generated (
  id                        bigint not null,
  name                      varchar(255),
  date                      timestamp,
  file                      blob,
  constraint pk_pdf_generated primary key (id))
;

create sequence pdf_generated_seq;




# --- !Downs

SET REFERENTIAL_INTEGRITY FALSE;

drop table if exists pdf_generated;

SET REFERENTIAL_INTEGRITY TRUE;

drop sequence if exists pdf_generated_seq;

