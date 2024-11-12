-- Table: sagademo.loan

-- DROP TABLE IF EXISTS sagademo.loan;

CREATE TABLE IF NOT EXISTS sagademo.loan
(
    id integer NOT NULL,
    amount numeric(9,2),
    applicantid integer,
    approved boolean,
    loanrequestdate timestamp with time zone,
    loancanceldate timestamp with time zone,
    loanapprovaldate timestamp with time zone,
    comment character varying(256) COLLATE pg_catalog."default",
    CONSTRAINT loan_pkey PRIMARY KEY (id)
)

TABLESPACE pg_default;

ALTER TABLE IF EXISTS sagademo.loan
    OWNER to abryson;

COMMENT ON TABLE sagademo.loan
    IS 'applicant / loan scenario.  this table will hold loan requests.';