CREATE TABLE IF NOT EXISTS User
(
    id         VARCHAR(50)  NOT NULL UNIQUE PRIMARY KEY,
    email      VARCHAR(50)  NOT NULL UNIQUE,
    password   VARCHAR(255) NOT NULL,
    phone      VARCHAR(15)  NOT NULL UNIQUE,
    nickname   VARCHAR(10),
    kakao_id   VARCHAR(50) UNIQUE,
    withdrawn  BOOLEAN DEFAULT FALSE,
    user_type  VARCHAR(15)  NOT NULL,
    created_at DATETIME(6)  NOT NULL,
    updated_at DATETIME(6)
) ENGINE = InnoDB;

CREATE TABLE IF NOT EXISTS Role
(
    user_id   VARCHAR(50) NOT NULL,
    role_name VARCHAR(15) NOT NULL,
    PRIMARY KEY (user_id, role_name),
    FOREIGN KEY (user_id) REFERENCES User (id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS Cafe
(
    id                           VARCHAR(50)   NOT NULL UNIQUE PRIMARY KEY,
    cafe_name                    VARCHAR(50)   NOT NULL,
    cafe_contact                 VARCHAR(15),
    cafe_introduction            VARCHAR(1000) NOT NULL,
    reward                       VARCHAR(100),
    required_stamps              INT,
    state                        VARCHAR(20)   NOT NULL,
    city                         VARCHAR(20)   NOT NULL,
    town                         VARCHAR(20)   NOT NULL,
    address_detail               VARCHAR(50),
    road_address                 VARCHAR(100)  NOT NULL,
    rating_average               DOUBLE        NOT NULL,
    favorites_count              INT           NOT NULL,
    business_registration_number VARCHAR(15)   NOT NULL,
    created_at                   DATETIME(6)   NOT NULL,
    updated_at                   DATETIME(6),
    user_id                      VARCHAR(50)   NOT NULL,
    FOREIGN KEY (user_id) REFERENCES User (id) ON DELETE CASCADE
) ENGINE = InnoDB;

CREATE TABLE IF NOT EXISTS Tag
(
    id         VARCHAR(50) NOT NULL UNIQUE PRIMARY KEY,
    tag_name   VARCHAR(10) NOT NULL UNIQUE,
    created_at DATETIME(6) NOT NULL,
    updated_at DATETIME(6)
) ENGINE = InnoDB;

CREATE TABLE IF NOT EXISTS Cafe_Tag
(
    cafe_id VARCHAR(50) NOT NULL,
    tag_id  VARCHAR(50) NOT NULL,
    PRIMARY KEY (cafe_id, tag_id),
    FOREIGN KEY (cafe_id) REFERENCES Cafe (id) ON DELETE CASCADE,
    FOREIGN KEY (tag_id) REFERENCES Tag (id)
) ENGINE = InnoDB;

CREATE TABLE IF NOT EXISTS Image
(
    id         VARCHAR(50)  NOT NULL UNIQUE PRIMARY KEY,
    image_url  VARCHAR(500) NOT NULL,
    cafe_id    VARCHAR(50)  NOT NULL,
    created_at DATETIME(6)  NOT NULL,
    updated_at DATETIME(6),
    FOREIGN KEY (cafe_id) REFERENCES Cafe (id) ON DELETE CASCADE
) ENGINE = InnoDB;

CREATE TABLE IF NOT EXISTS Menu
(
    id           VARCHAR(50) NOT NULL UNIQUE PRIMARY KEY,
    menu_name    VARCHAR(20) NOT NULL,
    menu_price   INT         NOT NULL,
    image_url    VARCHAR(500),
    likes_count  INT         NOT NULL,
    is_signature BOOLEAN DEFAULT TRUE,
    cafe_id      VARCHAR(50) NOT NULL,
    created_at   DATETIME(6) NOT NULL,
    updated_at   DATETIME(6),
    FOREIGN KEY (cafe_id) REFERENCES Cafe (id) ON DELETE CASCADE
) ENGINE = InnoDB;

CREATE TABLE IF NOT EXISTS Review
(
    id              VARCHAR(50)  NOT NULL UNIQUE PRIMARY KEY,
    review_contents VARCHAR(500) NOT NULL,
    star_rating     INT          NOT NULL,
    user_id         VARCHAR(50),
    cafe_id         VARCHAR(50)  NOT NULL,
    created_at      DATETIME(6)  NOT NULL,
    updated_at      DATETIME(6),
    FOREIGN KEY (user_id) REFERENCES User (id) ON DELETE SET NULL,
    FOREIGN KEY (cafe_id) REFERENCES Cafe (id) ON DELETE CASCADE
) ENGINE = InnoDB;

CREATE TABLE IF NOT EXISTS Coupon
(
    id         VARCHAR(50) NOT NULL UNIQUE PRIMARY KEY,
    stamps     INT         NOT NULL,
    user_id    VARCHAR(50) NOT NULL,
    cafe_id    VARCHAR(50) NOT NULL,
    created_at DATETIME(6) NOT NULL,
    updated_at DATETIME(6),
    FOREIGN KEY (user_id) REFERENCES User (id) ON DELETE CASCADE,
    FOREIGN KEY (cafe_id) REFERENCES Cafe (id) ON DELETE CASCADE
) ENGINE = InnoDB;

CREATE TABLE IF NOT EXISTS `Like`
(
    id         VARCHAR(50) NOT NULL UNIQUE PRIMARY KEY,
    user_id    VARCHAR(50) NOT NULL,
    menu_id    VARCHAR(50),
    created_at DATETIME(6) NOT NULL,
    updated_at DATETIME(6),
    FOREIGN KEY (user_id) REFERENCES User (id) ON DELETE CASCADE,
    FOREIGN KEY (menu_id) REFERENCES Menu (id) ON DELETE SET NULL,
    CONSTRAINT like_unique UNIQUE (user_id, menu_id)
) ENGINE = InnoDB;

CREATE TABLE IF NOT EXISTS Favorite
(
    id         VARCHAR(50) NOT NULL UNIQUE PRIMARY KEY,
    user_id    VARCHAR(50) NOT NULL,
    cafe_id    VARCHAR(50) NOT NULL,
    created_at DATETIME(6) NOT NULL,
    updated_at DATETIME(6),
    FOREIGN KEY (user_id) REFERENCES User (id) ON DELETE CASCADE,
    FOREIGN KEY (cafe_id) REFERENCES Cafe (id) ON DELETE CASCADE,
    CONSTRAINT favorite_unique UNIQUE (user_id, cafe_id)
) ENGINE = InnoDB;

CREATE TABLE IF NOT EXISTS Notification
(
    id                    VARCHAR(50)  NOT NULL UNIQUE PRIMARY KEY,
    notification_contents VARCHAR(100) NOT NULL,
    notification_type     VARCHAR(15)  NOT NULL,
    created_at            DATETIME(6)  NOT NULL,
    updated_at            DATETIME(6)
) ENGINE = InnoDB;

CREATE TABLE IF NOT EXISTS User_Notification
(
    user_id         VARCHAR(50) NOT NULL,
    notification_id VARCHAR(50) NOT NULL,
    `read`          BOOLEAN DEFAULT FALSE,
    PRIMARY KEY (user_id, notification_id),
    FOREIGN KEY (user_id) REFERENCES User (id) ON DELETE CASCADE,
    FOREIGN KEY (notification_id) REFERENCES Notification (id) ON DELETE CASCADE,
    CONSTRAINT user_notification_unique UNIQUE (user_id, notification_id)
) ENGINE = InnoDB;

CREATE TABLE IF NOT EXISTS Event
(
    id             VARCHAR(50)  NOT NULL UNIQUE PRIMARY KEY,
    event_title    VARCHAR(30)  NOT NULL,
    event_contents VARCHAR(500) NOT NULL,
    image_url      VARCHAR(500),
    event_url      VARCHAR(500),
    event_start    DATETIME(6)  NOT NULL,
    event_end      DATETIME(6)  NOT NULL,
    cafe_id        VARCHAR(50)  NOT NULL,
    created_at     DATETIME(6)  NOT NULL,
    updated_at     DATETIME(6),
    FOREIGN KEY (cafe_id) REFERENCES Cafe (id) ON DELETE CASCADE
) ENGINE = InnoDB;

CREATE TABLE IF NOT EXISTS User_Log
(
    id              VARCHAR(50)  NOT NULL UNIQUE PRIMARY KEY,
    log_type        VARCHAR(15)  NOT NULL,
    log_description VARCHAR(500) NOT NULL,
    `page`          VARCHAR(20)  NOT NULL,
    endpoint        VARCHAR(50)  NOT NULL,
    platform        VARCHAR(15),
    user_id         VARCHAR(50),
    created_at      DATETIME(6)  NOT NULL,
    updated_at      DATETIME(6),
    FOREIGN KEY (user_id) REFERENCES User (id) ON DELETE SET NULL
) ENGINE = InnoDB;
