--
-- author: Eitan Suez
--   org.: UptoData, Inc.
-- descr.: sql script for creating schema for ashkelon database for mysql
--   date: 9.16.2001
-- 
--

-- 1. drop previously created objects

DROP TABLE API;
DROP TABLE PACKAGE;
DROP TABLE CLASSTYPE;
DROP TABLE IMPL_INTERFACE;
DROP TABLE SUPERCLASS;
DROP TABLE AUTHOR;
DROP TABLE CLASS_AUTHOR;
DROP TABLE DOC;
DROP TABLE REFERENCE;
DROP TABLE MEMBER;
DROP TABLE FIELD;
DROP TABLE EXECMEMBER;
DROP TABLE PARAMETER;
DROP TABLE METHOD;
DROP TABLE THROWNEXCEPTION;
DROP TABLE CONSTRUCTOR;

DROP TABLE CLASS_ANCESTORS;
DROP TABLE TEMP_DELTA;


-- 2. schema tables & indices

CREATE TABLE DOC
(
  ID INT NOT NULL,
  DOC_TYPE INT,
  SINCE VARCHAR(75),
  -- DEPRECATED VARCHAR(100),
  DEPRECATED TEXT,
  -- SUMMARYDESCRIPTION VARCHAR(250),
  SUMMARYDESCRIPTION TEXT,
  DESCRIPTION TEXT,
  PRIMARY KEY (ID)
);

CREATE TABLE REFERENCE
(
  LABEL VARCHAR(60),
  SOURCEDOC_ID INT,
  REFDOC_ID INT,
  REFDOC_NAME TEXT,
  REFDOC_TYPE INT,
  IS_INLINE INT,
  INLINE_ORDER INT,
  INLINE_FIELD_TYPE INT
);

CREATE INDEX REF_IDX1 ON REFERENCE(SOURCEDOC_ID);
CREATE INDEX REF_IDX2 ON REFERENCE(REFDOC_ID);


CREATE TABLE API
(
  ID INT NOT NULL,
  NAME VARCHAR(60) NOT NULL,
  SUMMARYDESCRIPTION VARCHAR(250),
  DESCRIPTION TEXT,
  PUBLISHER VARCHAR(150),
  DOWNLOAD_URL VARCHAR(150),
  RELEASE_DATE DATE,
  VERSION VARCHAR(30),
  PRIMARY KEY (ID)
);

CREATE INDEX API_IDX1 ON API(NAME);


CREATE TABLE PACKAGE
(
  ID INT NOT NULL,
  NAME VARCHAR(60) NOT NULL,
  DOCID INT,
  API_ID INT,
  PRIMARY KEY (ID)
);

CREATE INDEX PKG_IDX1 ON PACKAGE(NAME);
CREATE INDEX PKG_IDX2 ON PACKAGE(DOCID);


CREATE TABLE CLASSTYPE -- extends type
(
  ID INT NOT NULL,
  NAME VARCHAR(100) NOT NULL,
  QUALIFIEDNAME VARCHAR(150) NOT NULL,
  TYPE INT,
  PACKAGEID INT,
--  SUPERCLASSID INT,
  SUPERCLASSNAME VARCHAR(150),
  ISABSTRACT INT,
  VERSION VARCHAR(100),
  DOCID INT,
  ISSTATIC INT,
  ISFINAL INT,
  ACCESSIBILITY INT,
  MODIFIER VARCHAR(31),
  CONTAININGCLASSNAME VARCHAR(150),
  PRIMARY KEY (ID)
);

CREATE INDEX CLASS_IDX1 ON CLASSTYPE(NAME);
CREATE INDEX CLASS_IDX2 ON CLASSTYPE(QUALIFIEDNAME);
CREATE INDEX CLASS_IDX3 ON CLASSTYPE(TYPE);
CREATE INDEX CLASS_IDX4 ON CLASSTYPE(PACKAGEID);
CREATE INDEX CLASS_IDX5 ON CLASSTYPE(SUPERCLASSNAME);
CREATE INDEX CLASS_IDX6 ON CLASSTYPE(DOCID);
CREATE INDEX CLASS_IDX7 ON CLASSTYPE(ISABSTRACT);
CREATE INDEX CLASS_IDX8 ON CLASSTYPE(ACCESSIBILITY);


CREATE TABLE IMPL_INTERFACE
(
  CLASSID INT,
  INTERFACEID INT,
  NAME VARCHAR(150) NOT NULL
);

CREATE INDEX IIMPL_IDX1 ON IMPL_INTERFACE(NAME);
CREATE INDEX IIMPL_IDX2 ON IMPL_INTERFACE(CLASSID);


--
-- classes have relationships to other entities such as interfaces
-- they implement and classes that they extend (inheritance).
-- the superclass relationship was one where both origin and
-- target relations used the same table.  this complicated the
-- ability to update and sever relationships.  so superclass
-- relationships were put in a separate table simply to ease
-- the ability to connect & sever cross-references between classes.
--
CREATE TABLE SUPERCLASS
(
  CLASSID INT,
  SUPERCLASSID INT,
  NAME VARCHAR(150) NOT NULL
);

CREATE INDEX SUPER_IDX1 ON SUPERCLASS(NAME);
CREATE INDEX SUPER_IDX2 ON SUPERCLASS(CLASSID);



-- table author provided with the intention of extending model to store more information
-- about java class authors
CREATE TABLE AUTHOR
(
  ID INT NOT NULL,
  NAME VARCHAR(120) NOT NULL,
  EMAIL VARCHAR(120),
  PRIMARY KEY (ID)
);

CREATE INDEX AUTH_IDX1 ON AUTHOR(NAME);


CREATE TABLE CLASS_AUTHOR
(
  CLASSID INT,
  AUTHORID INT
);

CREATE INDEX CLSAUTH_IDX1 ON CLASS_AUTHOR(AUTHORID);
CREATE INDEX CLSAUTH_IDX2 ON CLASS_AUTHOR(CLASSID);


CREATE TABLE MEMBER
(
  ID INT NOT NULL,
  CLASSID INT,
  DOCID INT,
  NAME VARCHAR(100) NOT NULL,
  QUALIFIEDNAME VARCHAR(150) NOT NULL,
  ISSTATIC INT,
  ISFINAL INT,
  ACCESSIBILITY INT,
  MODIFIER VARCHAR(51),
  TYPE INT,
  PRIMARY KEY (ID)
);

CREATE INDEX MEMBER_IDX1 ON MEMBER(CLASSID);
CREATE INDEX MEMBER_IDX2 ON MEMBER(NAME);
CREATE INDEX MEMBER_IDX3 ON MEMBER(DOCID);
CREATE INDEX MEMBER_IDX4 ON MEMBER(ISSTATIC);
CREATE INDEX MEMBER_IDX5 ON MEMBER(ACCESSIBILITY);


CREATE TABLE FIELD -- extends member
(
  ID INT NOT NULL,
  TYPEID INT,
  TYPEDIMENSION INT,
  TYPENAME VARCHAR(150) NOT NULL,
  PRIMARY KEY (ID)
);

-- an apparent issue with mysql interpreting sql containing the string "FIELD(" -- must separate FIELD and ( with a space
CREATE INDEX FLD_IDX1 ON FIELD (TYPENAME);


CREATE TABLE EXECMEMBER -- extends member
(
  ID INT NOT NULL,
  ISSYNCHRONIZED INT,
  ISNATIVE INT,
  SIGNATURE TEXT NOT NULL, -- signature is parameter list in parentheses
  FULLYQUALIFIEDNAME TEXT NOT NULL, -- qualified name + signature
  PRIMARY KEY (ID)
);


CREATE TABLE METHOD -- extends execmember
(
  ID INT NOT NULL,
  ISABSTRACT INT,
  RETURNTYPEID INT,
  RETURNTYPEDIMENSION INT,
  RETURNTYPENAME VARCHAR(150) NOT NULL,
  RETURNDESCRIPTION TEXT,
  PRIMARY KEY (ID)
);

CREATE INDEX METHOD_IDX1 ON METHOD(RETURNTYPENAME);


CREATE TABLE CONSTRUCTOR -- extends execmember
(
  ID INT NOT NULL,
  PRIMARY KEY (ID)
);


CREATE TABLE PARAMETER
(
  EXECMEMBERID INT,
  NAME VARCHAR(50),
  DESCRIPTION TEXT,
  TYPEID INT,
  TYPEDIMENSION INT,
  TYPENAME VARCHAR(150) NOT NULL,
  LISTEDORDER INT
);

CREATE INDEX PARM_IDX1 ON PARAMETER(EXECMEMBERID);
CREATE INDEX PARM_IDX2 ON PARAMETER(TYPENAME);


CREATE TABLE THROWNEXCEPTION
(
  THROWERID INT,
  EXCEPTIONID INT,  -- references classtype(id) on delete sever!
  NAME VARCHAR(150) NOT NULL,
  DESCRIPTION TEXT
);

CREATE INDEX THROW_IDX1 ON THROWNEXCEPTION(NAME);
CREATE INDEX THROW_IDX2 ON THROWNEXCEPTION(THROWERID);


-- following two tables added 11.7.2001 / e.suez
-- reason: these provide an alternate method of generating tree structures without
--  using connect by prior statements.
CREATE TABLE CLASS_ANCESTORS
(
  CLASSID INT  NOT NULL,
  SUPERCLASSID INT NOT NULL,
  HIERARCHY INT NOT NULL
);

CREATE INDEX ANC_IDX1 ON CLASS_ANCESTORS(CLASSID);
CREATE INDEX ANC_IDX2 ON CLASS_ANCESTORS(SUPERCLASSID);

-- used as temporary 'delta' table in populating class_ancestors table
CREATE TABLE TEMP_DELTA
(
  CLASSID INT  NOT NULL,
  SUPERCLASSID INT NOT NULL,
  HIERARCHY INT NOT NULL
);



-- 3. sequence generator provides db independence (may switch to db sequences at a later time)

DROP TABLE SEQS;

CREATE TABLE SEQS
(
  SEQNAME VARCHAR(25) NOT NULL,
  SEQVAL INT,
  PRIMARY KEY (SEQNAME)
);


-- sequence information
INSERT INTO SEQS (SEQNAME, SEQVAL) VALUES ('PKG_SEQ', 100);
INSERT INTO SEQS (SEQNAME, SEQVAL) VALUES ('CLASSTYPE_SEQ', 100);
INSERT INTO SEQS (SEQNAME, SEQVAL) VALUES ('AUTHOR_SEQ', 100);
INSERT INTO SEQS (SEQNAME, SEQVAL) VALUES ('DOC_SEQ', 100);
INSERT INTO SEQS (SEQNAME, SEQVAL) VALUES ('MEMBER_SEQ', 100);
INSERT INTO SEQS (SEQNAME, SEQVAL) VALUES ('API_SEQ', 100);

