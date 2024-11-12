--
-- PostgreSQL database dump
--

-- Dumped from database version 15.8 (Homebrew)
-- Dumped by pg_dump version 16.4

-- Started on 2024-11-05 17:55:34 EST

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- TOC entry 240 (class 1259 OID 24576)
-- Name: loan; Type: TABLE; Schema: sagademo; Owner: abryson
--

CREATE TABLE sagademo.loan (
    id integer NOT NULL,
    amount numeric(9,2),
    applicantid integer,
    approved boolean,
    loanrequestdate timestamp with time zone,
    loancanceldate timestamp with time zone,
    loanapprovaldate timestamp with time zone,
    comment character varying(256)
);


ALTER TABLE sagademo.loan OWNER TO abryson;

--
-- TOC entry 3708 (class 0 OID 0)
-- Dependencies: 240
-- Name: TABLE loan; Type: COMMENT; Schema: sagademo; Owner: abryson
--

COMMENT ON TABLE sagademo.loan IS 'applicant / loan scenario.  this table will hold loan requests.';


--
-- TOC entry 3702 (class 0 OID 24576)
-- Dependencies: 240
-- Data for Name: loan; Type: TABLE DATA; Schema: sagademo; Owner: abryson
--

INSERT INTO sagademo.loan (id, amount, applicantid, approved, loanrequestdate, loancanceldate, loanapprovaldate, comment) VALUES (1, 50.00, 1, false, '2023-12-15 13:27:01-05', NULL, NULL, NULL);
INSERT INTO sagademo.loan (id, amount, applicantid, approved, loanrequestdate, loancanceldate, loanapprovaldate, comment) VALUES (2, 150.00, 2, false, '2023-06-22 18:45:01-04', NULL, NULL, NULL);
INSERT INTO sagademo.loan (id, amount, applicantid, approved, loanrequestdate, loancanceldate, loanapprovaldate, comment) VALUES (3, 1000.00, 3, false, '2022-01-19 08:45:00-05', NULL, NULL, NULL);


--
-- TOC entry 3559 (class 2606 OID 24580)
-- Name: loan loan_pkey; Type: CONSTRAINT; Schema: sagademo; Owner: abryson
--

ALTER TABLE ONLY sagademo.loan
    ADD CONSTRAINT loan_pkey PRIMARY KEY (id);


-- Completed on 2024-11-05 17:55:34 EST

--
-- PostgreSQL database dump complete
--

