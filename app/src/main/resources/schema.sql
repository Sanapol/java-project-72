DROP TABLE IF EXISTS urls;

CREATE TABLE urls (
id long PRIMARY KEY AUTO_INCREMENT,
name VARCHAR(255) NOT NULL,
created_at TIMESTAMP
);

DROP TABLE IF EXISTS url_checks;

CREATE TABLE url_checks (
id long PRIMARY KEY AUTO_INCREMENT,
url_id long,
status_code INT,
title VARCHAR(255),
h1 VARCHAR(255),
description text,
created_at TIMESTAMP
);