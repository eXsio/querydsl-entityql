create table JBOOKS (BOOK_ID bigint not null, DESC CLOB, NAME varchar(255), PRICE decimal(19,2), primary key (BOOK_ID));
alter table JBOOKS add constraint UK_j5kgaosmuibg20yalaplmf43t unique (NAME);