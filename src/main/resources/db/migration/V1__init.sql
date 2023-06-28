-- users table
CREATE SEQUENCE public.users_pkey_seq;

ALTER SEQUENCE public.users_pkey_seq
    OWNER TO postgres;

CREATE TABLE public.users
(
    id         bigint    NOT NULL DEFAULT nextval('users_pkey_seq'),
    chat_id    bigint    NOT NULL,
    timezone   integer,
    created_at timestamp NOT NULL,
    updated_at timestamp,
    PRIMARY KEY (id)
);

ALTER TABLE IF EXISTS public.users
    OWNER to postgres;

-- words table
CREATE SEQUENCE public.words_pkey_seq;

ALTER SEQUENCE public.words_pkey_seq
    OWNER TO postgres;

CREATE TABLE public.words
(
    id          bigint      NOT NULL DEFAULT nextval('words_pkey_seq'),
    word        varchar(30) NOT NULL,
    created_at  timestamp   NOT NULL,
    img_s3_path varchar(255),
    PRIMARY KEY (id)
);

ALTER TABLE IF EXISTS public.words
    OWNER to postgres;

-- saved_words table
CREATE SEQUENCE public.saved_words_pkey_seq;

ALTER SEQUENCE public.saved_words_pkey_seq
    OWNER TO postgres;

CREATE TABLE public.saved_words
(
    id      bigint NOT NULL DEFAULT nextval('saved_words_pkey_seq'),
    user_id bigint NOT NULL
        constraint saved_words_to_users_fk references users,
    word_id bigint NOT NULL
        constraint saved_words_to_words_fk references words,
    PRIMARY KEY (id)
);

ALTER TABLE IF EXISTS public.subscriptions
    OWNER to postgres;