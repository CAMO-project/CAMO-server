CREATE TABLE IF NOT EXISTS User
(
    id         VARCHAR(50) NOT NULL UNIQUE PRIMARY KEY,
    email      VARCHAR(50) NOT NULL UNIQUE,
    password   VARCHAR(50) NOT NULL,
    phone      VARCHAR(15) NOT NULL UNIQUE,
    nickname   VARCHAR(10),
    kakao_id   VARCHAR(50),
    withdrawn  BOOLEAN DEFAULT FALSE,
    created_at DATETIME(6),
    updated_at DATETIME(6)
) ENGINE = InnoDB;

CREATE TABLE IF NOT EXISTS Cafe
(
    id                VARCHAR(50)   NOT NULL UNIQUE PRIMARY KEY,
    cafe_name         VARCHAR(20)   NOT NULL,
    cafe_contact      VARCHAR(15),
    cafe_introduction VARCHAR(1000) NOT NULL,
    reward            VARCHAR(100),
    required_stamps   INT,
    created_at        DATETIME(6),
    updated_at        DATETIME(6)
) ENGINE = InnoDB;

CREATE TABLE IF NOT EXISTS Image
(
    id         VARCHAR(50)  NOT NULL UNIQUE PRIMARY KEY,
    image_url  VARCHAR(200) NOT NULL UNIQUE,
    created_at DATETIME(6),
    updated_at DATETIME(6)
) ENGINE = InnoDB;

CREATE TABLE IF NOT EXISTS Cafe_Image
(
    cafe_id  VARCHAR(50) NOT NULL,
    image_id VARCHAR(50) NOT NULL,
    PRIMARY KEY (cafe_id, image_id),
    FOREIGN KEY (cafe_id) REFERENCES Cafe (id) ON DELETE CASCADE,
    FOREIGN KEY (image_id) REFERENCES Image (id)
) ENGINE = InnoDB;

CREATE TABLE IF NOT EXISTS Address
(
    city VARCHAR(10) NOT NULL,
    town VARCHAR(10) NOT NULL
) ENGINE = InnoDB;

CREATE TABLE IF NOT EXISTS Menu
(
    id             VARCHAR(50) NOT NULL UNIQUE PRIMARY KEY,
    menu_name      VARCHAR(20) NOT NULL,
    menu_price     INT         NOT NULL,
    menu_image_url VARCHAR(200),
    cafe_id        VARCHAR(50) NOT NULL,
    created_at     DATETIME(6),
    updated_at     DATETIME(6),
    FOREIGN KEY (cafe_id) REFERENCES Cafe (id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS Review
(
    id              VARCHAR(50)  NOT NULL UNIQUE PRIMARY KEY,
    review_contents VARCHAR(500) NOT NULL,
    review_date     DATETIME(6)  NOT NULL,
    user_id         VARCHAR(50)  NOT NULL,
    created_at      DATETIME(6),
    updated_at      DATETIME(6),
    FOREIGN KEY (user_id) REFERENCES User (id) ON DELETE SET NULL
);

CREATE TABLE IF NOT EXISTS Coupon
(
    id         VARCHAR(50) NOT NULL UNIQUE PRIMARY KEY,
    stamps     INT         NOT NULL,
    user_id    VARCHAR(50) NOT NULL,
    cafe_id    VARCHAR(50) NOT NULL,
    created_at DATETIME(6),
    updated_at DATETIME(6),
    FOREIGN KEY (user_id) REFERENCES User (id) ON DELETE CASCADE,
    FOREIGN KEY (cafe_id) REFERENCES Cafe (id) ON DELETE SET NULL
);
