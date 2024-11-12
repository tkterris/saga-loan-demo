--
-- PostgreSQL database dump
--

-- Dumped from database version 15.8 (Homebrew)
-- Dumped by pg_dump version 16.4

-- Started on 2024-11-05 17:58:25 EST

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

DROP DATABASE sagademo;
--
-- TOC entry 3708 (class 1262 OID 16388)
-- Name: sagademo; Type: DATABASE; Schema: -; Owner: abryson
--

CREATE DATABASE sagademo WITH TEMPLATE = template0 ENCODING = 'UTF8' LOCALE_PROVIDER = libc LOCALE = 'C';


ALTER DATABASE sagademo OWNER TO abryson;

\connect sagademo

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

--
-- TOC entry 3709 (class 0 OID 0)
-- Dependencies: 3708
-- Name: DATABASE sagademo; Type: COMMENT; Schema: -; Owner: abryson
--

COMMENT ON DATABASE sagademo IS 'saga demo database';


SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- TOC entry 241 (class 1259 OID 24581)
-- Name: applicant; Type: TABLE; Schema: sagademo; Owner: abryson
--

CREATE TABLE sagademo.applicant (
    id integer NOT NULL,
    "limit" numeric(9,2),
    approved boolean,
    limitupdatedate timestamp with time zone,
    comment character varying(256)
);


ALTER TABLE sagademo.applicant OWNER TO abryson;

--
-- TOC entry 3710 (class 0 OID 0)
-- Dependencies: 241
-- Name: TABLE applicant; Type: COMMENT; Schema: sagademo; Owner: abryson
--

COMMENT ON TABLE sagademo.applicant IS 'Application / Loan scenario.  Applicant table.';


--
-- TOC entry 3702 (class 0 OID 24581)
-- Dependencies: 241
-- Data for Name: applicant; Type: TABLE DATA; Schema: sagademo; Owner: abryson
--

INSERT INTO sagademo.applicant (id, "limit", approved, limitupdatedate, comment) VALUES (1, 300.00, false, NULL, 'Nico Iamaleava applicant');
INSERT INTO sagademo.applicant (id, "limit", approved, limitupdatedate, comment) VALUES (2, 600.00, false, NULL, 'Dylan Sampson applicant');
INSERT INTO sagademo.applicant (id, "limit", approved, limitupdatedate, comment) VALUES (3, 900.00, false, NULL, 'James Pearce applicant');


--
-- TOC entry 3559 (class 2606 OID 24585)
-- Name: applicant applicant_pkey; Type: CONSTRAINT; Schema: sagademo; Owner: abryson
--

ALTER TABLE ONLY sagademo.applicant
    ADD CONSTRAINT applicant_pkey PRIMARY KEY (id);


-- Completed on 2024-11-05 17:58:25 EST

--
-- PostgreSQL database dump complete
--

