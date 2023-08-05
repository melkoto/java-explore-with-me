CREATE TABLE IF NOT EXISTS compilations
(
    id     BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    pinned BOOLEAN,
    title  VARCHAR(255)                            NOT NULL,
    CONSTRAINT PK_COMPILATION PRIMARY KEY (id),
    CONSTRAINT UQ_COMPILATION_TITLE UNIQUE (title)
);

CREATE TABLE IF NOT EXISTS categories
(
    id   INTEGER GENERATED BY DEFAULT AS IDENTITY,
    name VARCHAR(255) NOT NULL,
    CONSTRAINT PK_CATEGORIES PRIMARY KEY (id),
    CONSTRAINT UQ_CATEGORY UNIQUE (name)
);

CREATE TABLE IF NOT EXISTS users
(
    id    BIGINT GENERATED BY DEFAULT AS IDENTITY,
    name  VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL,
    CONSTRAINT PK_USERS PRIMARY KEY (id),
    CONSTRAINT UQ_USER UNIQUE (email)
);

CREATE TABLE IF NOT EXISTS events
(
    id                 BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    annotation         VARCHAR(2000)                           NOT NULL,
    category_id        INTEGER                                 NOT NULL,
    created_on         TIMESTAMP                               NOT NULL,
    description        VARCHAR(7000)                           NOT NULL,
    event_date         TIMESTAMP                               NOT NULL,
    initiator_id       BIGINT                                  NOT NULL,
    location_id        BIGINT                                  NOT NULL,
    paid               BOOLEAN DEFAULT FALSE,
    participant_limit  INTEGER DEFAULT 0,
    published_on       TIMESTAMP,
    request_moderation BOOLEAN DEFAULT TRUE,
    state              VARCHAR(50)                             NOT NULL,
    title              VARCHAR(120)                            NOT NULL,
    views              INTEGER DEFAULT 0                       NOT NULL,
    CONSTRAINT pk_events PRIMARY KEY (id),
    CONSTRAINT fk_events_users FOREIGN KEY (initiator_id) REFERENCES users (id) ON DELETE SET NULL ON UPDATE CASCADE,
    CONSTRAINT fk_events_categories FOREIGN KEY (category_id) REFERENCES categories (id) ON DELETE SET NULL ON UPDATE CASCADE
);

CREATE TABLE IF NOT EXISTS comments
(
    id         BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    text       VARCHAR(1000) NOT NULL,
    author_id  BIGINT REFERENCES users (id) ON DELETE CASCADE,
    event_id   BIGINT REFERENCES events (id) ON DELETE CASCADE,
    created_on TIMESTAMP     NOT NULL,
    edited_on  TIMESTAMP
);
