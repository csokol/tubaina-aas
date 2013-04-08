# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table pdf_generated (
  id                        bigint auto_increment not null,
  name                      varchar(255),
  date                      datetime,
  file                      longblob,
  constraint pk_pdf_generated primary key (id))
;




# --- !Downs

SET FOREIGN_KEY_CHECKS=0;

drop table pdf_generated;

SET FOREIGN_KEY_CHECKS=1;

