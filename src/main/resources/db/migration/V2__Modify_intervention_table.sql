DROP TABLE intervention;

CREATE SEQUENCE public.revision_info_id_sequence INCREMENT 1 START 1 MINVALUE 1;

CREATE TABLE revision_info
(
    id bigint PRIMARY KEY,
    timestamp BIGINT,
    username CHARACTER VARYING(255)
);

CREATE TABLE provider (
    id uuid PRIMARY KEY,
    name CHARACTER VARYING(255) NOT NULL,
    delius_code CHARACTER VARYING(20) NOT NULL,
    active boolean NOT NULL,
    created_date timestamp with time zone NOT NULL
);

CREATE TABLE provider_aud (
    id uuid NOT NULL,
    name CHARACTER VARYING(255),
    delius_code CHARACTER VARYING(20),
    active boolean,
    created_date timestamp with time zone,
    rev integer NOT NULL,
    revtype integer,
    PRIMARY KEY ( id, REV ),
    FOREIGN KEY ( rev ) REFERENCES revision_info
);

CREATE TABLE intervention_type (
    id uuid PRIMARY KEY,
    name CHARACTER VARYING(255) NOT NULL,
    delius_code CHARACTER VARYING(20) NOT NULL,
    active boolean NOT NULL,
    created_date timestamp with time zone NOT NULL
);

CREATE TABLE intervention_type_aud (
    id uuid NOT NULL,
    name CHARACTER VARYING(255),
    delius_code CHARACTER VARYING(20),
    active boolean,
    created_date timestamp with time zone,
    rev integer NOT NULL,
    revtype integer,
    PRIMARY KEY ( id, REV ),
    FOREIGN KEY ( rev ) REFERENCES revision_info
);

CREATE TABLE intervention_sub_type (
    id uuid PRIMARY KEY,
    intervention_type_id uuid NOT NULL,
    name CHARACTER VARYING(255) NOT NULL,
    delius_code CHARACTER VARYING(20) NOT NULL,
    active boolean NOT NULL,
    created_date timestamp with time zone NOT NULL,
    CONSTRAINT intervention_sub_type_intervention_type FOREIGN KEY (intervention_type_id) REFERENCES intervention_type (id)
);

CREATE TABLE intervention_sub_type_aud (
    id uuid NOT NULL,
    intervention_type_id uuid,
    name CHARACTER VARYING(255),
    delius_code CHARACTER VARYING(20),
    active boolean,
    created_date timestamp with time zone,
	rev integer NOT NULL,
    revtype integer,
    PRIMARY KEY ( id, REV ),
    FOREIGN KEY ( rev ) REFERENCES revision_info,
    CONSTRAINT intervention_sub_type_aud_intervention_type FOREIGN KEY (intervention_type_id) REFERENCES intervention_type (id)
);

CREATE TABLE provider_intervention_type (
    provider_id uuid NOT NULL,
    intervention_type_id uuid NOT NULL,
    created_date timestamp with time zone,
    CONSTRAINT provider_intervention_type_provider FOREIGN KEY (provider_id) REFERENCES provider (id),
    CONSTRAINT provider_intervention_type_intervention_type FOREIGN KEY (intervention_type_id) REFERENCES intervention_type (id)
);

CREATE TABLE provider_intervention_type_aud (
    provider_id uuid,
    intervention_type_id uuid,
    created_date timestamp with time zone,
    rev integer NOT NULL,
    revtype integer,
    FOREIGN KEY ( rev ) REFERENCES revision_info,
    CONSTRAINT provider_intervention_type_aud_provider FOREIGN KEY (provider_id) REFERENCES provider (id),
    CONSTRAINT provider_intervention_type_aud_intervention_type FOREIGN KEY (intervention_type_id) REFERENCES intervention_type (id)
);
