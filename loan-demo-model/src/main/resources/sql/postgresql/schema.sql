\connect sagademo;

CREATE SEQUENCE IF NOT EXISTS sagademo.applicant_id_seq
    INCREMENT 50
    START 1
    MINVALUE 1
    MAXVALUE 9223372036854775807
    CACHE 1;

CREATE TABLE sagademo.applicant
(
    id integer NOT NULL,
    "limit" numeric(38,2),
    approved boolean,
    limitupdatedate timestamp with time zone,
    comment character varying(255) COLLATE pg_catalog."default",
    limitused numeric(38,2),
    CONSTRAINT applicant_pkey PRIMARY KEY (id)
);

ALTER SEQUENCE sagademo.applicant_id_seq
    OWNED BY sagademo.applicant.id;

--- TABLESPACE pg_default;

ALTER TABLE sagademo.applicant
    OWNER to sagademo;

COMMENT ON TABLE sagademo.applicant
    IS 'Application / Loan scenario.  Applicant table.';
    
CREATE SEQUENCE IF NOT EXISTS sagademo.loan_id_seq
    INCREMENT 1
    START 1
    MINVALUE 1
    MAXVALUE 2147483647
    CACHE 1;

CREATE TABLE sagademo.loan
(
    id integer NOT NULL DEFAULT nextval('sagademo.loan_id_seq'::regclass),
    amount numeric(38,2),
    applicantid integer,
    approved boolean,
    loanrequestdate timestamp with time zone,
    loancanceldate timestamp with time zone,
    loanapprovaldate timestamp with time zone,
    comment character varying(255) COLLATE pg_catalog."default",
    CONSTRAINT loan_pkey PRIMARY KEY (id)
);

ALTER SEQUENCE sagademo.loan_id_seq
    OWNED BY sagademo.loan.id;

--- TABLESPACE pg_default;

ALTER TABLE sagademo.loan
    OWNER to sagademo;

COMMENT ON TABLE sagademo.loan
    IS 'applicant / loan scenario.  this table will hold loan requests.';

