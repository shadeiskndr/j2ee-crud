CREATE TABLE IF NOT EXISTS members (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    join_date DATE NOT NULL,
    ic_number VARCHAR(20) NOT NULL UNIQUE,
    gender VARCHAR(10) NOT NULL,
    date_of_birth DATE NOT NULL,
    postcode VARCHAR(10) NOT NULL,
    town VARCHAR(100) NOT NULL
);

CREATE TABLE IF NOT EXISTS states (
    state_code VARCHAR(5) PRIMARY KEY,
    state_name VARCHAR(100) NOT NULL
);

CREATE TABLE IF NOT EXISTS postcodes (
    postcode VARCHAR(10) PRIMARY KEY,
    town VARCHAR(100) NOT NULL,
    state_code VARCHAR(5) NOT NULL,
    FOREIGN KEY (state_code) REFERENCES states(state_code)
);

