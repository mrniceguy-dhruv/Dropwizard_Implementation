# Dropwizard Implementation With Hibernate and Basic Authentication

## Config YAML
<p> For mysql database connectivity, enter user,password,database location and driver name. </p>

## Mysql Databse
- Database creation:
```
create database DWGettingStarted;
use DWGettingStarted;
```
- Employee table creation:
```
create table employees(
    -- auto-generated primary key
    id bigint primary key not null auto_increment,
    first_name varchar(255) not null,
    last_name varchar(255) not null,
    -- employee position
    e_position  varchar(255) not null,
    phone  varchar(255) not null,
    e_mail varchar(255) not null
);
```
- User table creation for Authentication, insert some values:
```
create table users(
	id bigint not null primary key auto_increment,
    username varchar(255) not null,
    password varchar(255) not null
);
```

## Run
command: java -jar target/Dropwizard_Hibernate-1.0-SNAPSHOT.jar server config.yml <br>
Prefer Postman

## Resources

- Getting Started [Hello World](https://www.dropwizard.io/en/latest/getting-started.html).
- GET,PUT with locally declared hashmap [link](https://howtodoinjava.com/dropwizard/tutorial-and-hello-world-example/).
- Hibernate overview [tutorial](https://www.youtube.com/watch?v=VtCz0oPtfG0&ab_channel=edureka%21).
- Databse Connectivity with Hibernate [link](https://dzone.com/articles/getting-started-with-dropwizard-connecting-to-a-da).
- Authentication with databse [link](https://github.com/javaeeeee/DropBookmarks).
- GET,DELETE operations [link](https://github.com/javaeeeee/DropBookmarks).
- POST,PUT operation implemented in the current repository.


