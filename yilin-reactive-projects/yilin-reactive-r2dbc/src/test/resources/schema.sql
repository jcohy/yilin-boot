CREATE TABLE person
(
    id      integer AUTO_INCREMENT CONSTRAINT id1 PRIMARY KEY,
    version integer      NULL,
    name    varchar(255) NOT NULL,
    deleted integer      NOT NULL,
    status  integer      NOT NULL,
    age     integer      NULL
);