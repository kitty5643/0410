CREATE TABLE messageTopic (
    id INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),
    category VARCHAR(20) CHECK(category in('lab', 'lecture', 'other')),
    username VARCHAR(50),
    title VARCHAR(50),
    message VARCHAR(200),
    date TIMESTAMP,
    PRIMARY KEY (id),
    FOREIGN KEY (username) REFERENCES users(username)
);

CREATE TABLE messageReply (
    id INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),
    username VARCHAR(50),
    message VARCHAR(200),
    id_topic INTEGER NOT NULL,
    date TIMESTAMP,
    PRIMARY KEY (id),
    FOREIGN KEY (username) REFERENCES users(username),
    FOREIGN KEY (id_topic) REFERENCES messageTopic(id)
);


CREATE TABLE attachmentTopic (
    id INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),
    file_name VARCHAR(50),
    file_data BLOB,
    mime VARCHAR(100),
    id_topic INTEGER,
    PRIMARY KEY (id),
    FOREIGN KEY (id_topic) REFERENCES messageTopic(id)
);


insert into messageReply(message) values('testing');