DROP TABLE intervention;

CREATE TABLE provider (
    id uuid PRIMARY KEY,
    name CHARACTER VARYING(255) NOT NULL,
    created_date timestamp with time zone NOT NULL
);

CREATE TABLE intervention_type (
    id uuid PRIMARY KEY,
    name CHARACTER VARYING(255) NOT NULL
);

CREATE TABLE intervention_sub_type (
    id uuid PRIMARY KEY,
    intervention_type_id uuid NOT NULL,
    name CHARACTER VARYING(255) NOT NULL,
    CONSTRAINT intervention_sub_type_intervention_type FOREIGN KEY (intervention_type_id) REFERENCES intervention_type (id)
);

CREATE TABLE provider_intervention_type (
    provider_id uuid NOT NULL,
    intervention_type_id uuid NOT NULL,
    CONSTRAINT provider_intervention_type_provider FOREIGN KEY (provider_id) REFERENCES provider (id),
    CONSTRAINT provider_intervention_type_intervention_type FOREIGN KEY (intervention_type_id) REFERENCES intervention_type (id)
);

