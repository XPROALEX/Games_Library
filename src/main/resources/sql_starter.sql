CREATE DATABASE IF NOT EXISTS gamelibrary;

USE gamelibrary;

CREATE TABLE IF NOT EXISTS users(
    id       BIGINT unsigned NOT NULL AUTO_INCREMENT,
    username VARCHAR(255) DEFAULT NULL,
    password VARCHAR(255) DEFAULT NULL,
    email    VARCHAR(255) DEFAULT NULL,
    PRIMARY KEY (id),
    UNIQUE (username, email))
    ENGINE = InnoDB DEFAULT CHARSET = UTF8;

CREATE TABLE IF NOT EXISTS roles(
    id   int unsigned   NOT NULL AUTO_INCREMENT,
    name ENUM ('ROLE_USER', 'ROLE_MODERATOR', 'ROLE_ADMIN') NOT NULL,
    PRIMARY KEY (id))
    ENGINE = InnoDB DEFAULT CHARSET = UTF8;

CREATE TABLE IF NOT EXISTS games(
    id          BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
    name        VARCHAR(255)    NOT NULL,
    description VARCHAR(255),
    PRIMARY KEY (id),
    UNIQUE (name))
    ENGINE = InnoDB DEFAULT CHARSET = UTF8;

CREATE TABLE IF NOT EXISTS genres(
    id   INT UNSIGNED NOT NULL AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL,
    PRIMARY KEY (id),
    UNIQUE (name))
    ENGINE = InnoDB DEFAULT CHARSET = UTF8;

CREATE TABLE IF NOT EXISTS tokens(
    id          BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
    user_id     BIGINT UNSIGNED NOT NULL,
    token       VARCHAR(255)    NOT NULL UNIQUE,
    expiry_date TIMESTAMP       NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE) ENGINE = InnoDB DEFAULT CHARSET = UTF8;

CREATE TABLE IF NOT EXISTS user_roles(
    user_id BIGINT UNSIGNED NOT NULL,
    role_id INT UNSIGNED    NOT NULL,
    PRIMARY KEY (user_id, role_id),
    FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE,
    FOREIGN KEY (role_id) REFERENCES roles (id) ON DELETE CASCADE)
    ENGINE = InnoDB DEFAULT CHARSET = UTF8;

CREATE TABLE IF NOT EXISTS user_games(
    user_id BIGINT UNSIGNED NOT NULL,
    game_id BIGINT UNSIGNED NOT NULL,
    PRIMARY KEY (user_id, game_id),
    FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE,
    FOREIGN KEY (game_id) REFERENCES games (id) ON DELETE CASCADE)
    ENGINE = InnoDB DEFAULT CHARSET = UTF8;

CREATE TABLE IF NOT EXISTS game_genre(
    game_id  BIGINT UNSIGNED NOT NULL,
    genre_id INT UNSIGNED    NOT NULL,
    PRIMARY KEY (game_id, genre_id),
    FOREIGN KEY (game_id) REFERENCES games (id) ON DELETE CASCADE,
    FOREIGN KEY (genre_id) REFERENCES genres (id) ON DELETE CASCADE)
    ENGINE = InnoDB DEFAULT CHARSET = UTF8;

INSERT INTO roles(name)VALUES ('ROLE_USER');
INSERT INTO roles(name)VALUES ('ROLE_MODERATOR');
INSERT INTO roles(name)VALUES ('ROLE_ADMIN');