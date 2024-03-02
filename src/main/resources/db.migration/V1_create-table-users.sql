CREATE TABLE users(
                      id VARCHAR(10) PRIMARY KEY UNIQUE NOT NULL,
                      login VARCHAR(80) NOT NULL UNIQUE,
                      password VARCHAR(20) NOT NULL,
                      role VARCHAR(20) NOT NULL
);