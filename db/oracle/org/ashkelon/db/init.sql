--
-- author: Eitan Suez
--   org.: UptoData, Inc.
-- descr.: sql script for creating schema for ashkelon database
--   date: 3.19.2001
--

--
-- very important note / discovery:
--  if this script is executed by a java stored procedure,  a commit will fail (silently)
--   on any tables that mix create/drop table statements with insert statements!
--

-- sequence information
DELETE FROM SEQS;
INSERT INTO SEQS (SEQNAME, SEQVAL) VALUES ('PKG_SEQ', 100);
INSERT INTO SEQS (SEQNAME, SEQVAL) VALUES ('CLASSTYPE_SEQ', 100);
INSERT INTO SEQS (SEQNAME, SEQVAL) VALUES ('AUTHOR_SEQ', 100);
INSERT INTO SEQS (SEQNAME, SEQVAL) VALUES ('DOC_SEQ', 100);
INSERT INTO SEQS (SEQNAME, SEQVAL) VALUES ('MEMBER_SEQ', 100);

--
-- tried to use oracle sequences - failed.  get synchronization errors from odbc driver each time
-- i try to get a nextval (POL-6063 Function sequence error - an ODBC Error S1010)
-- anyhow for a process such as dbdoc mgr it's more efficient to have a memory sequence generator
-- and commit once at the end
--

--DROP SEQUENCE PKG_SEQ;
--DROP SEQUENCE CLASSTYPE_SEQ;
--DROP SEQUENCE AUTHOR_SEQ;
--DROP SEQUENCE DOC_SEQ;
--DROP SEQUENCE MEMBER_SEQ;

--CREATE SEQUENCE PKG_SEQ;
--CREATE SEQUENCE CLASSTYPE_SEQ;
--CREATE SEQUENCE AUTHOR_SEQ;
--CREATE SEQUENCE DOC_SEQ;
--CREATE SEQUENCE MEMBER_SEQ;


--COMMIT;
