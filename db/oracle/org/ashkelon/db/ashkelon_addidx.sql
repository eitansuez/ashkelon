--
-- author: Eitan Suez
--   org.: UptoData, Inc.
-- descr.: sql script for creating indices on tables after adds
--   date: 3.13.2001
--

CREATE INDEX REF_IDX1 ON REFERENCE(SOURCEDOC_ID);
CREATE INDEX REF_IDX2 ON REFERENCE(REFDOC_ID);

CREATE INDEX PKG_IDX1 ON PACKAGE(NAME);
CREATE INDEX PKG_IDX2 ON PACKAGE(DOCID);

CREATE INDEX CLASS_IDX1 ON CLASSTYPE(NAME);
CREATE INDEX CLASS_IDX2 ON CLASSTYPE(QUALIFIEDNAME);
CREATE INDEX CLASS_IDX3 ON CLASSTYPE(TYPE);
CREATE INDEX CLASS_IDX4 ON CLASSTYPE(PACKAGEID);
CREATE INDEX CLASS_IDX5 ON CLASSTYPE(SUPERCLASSNAME);
CREATE INDEX CLASS_IDX6 ON CLASSTYPE(DOCID);
CREATE INDEX CLASS_IDX7 ON CLASSTYPE(ISABSTRACT);
CREATE INDEX CLASS_IDX8 ON CLASSTYPE(ACCESSIBILITY);

CREATE INDEX IIMPL_IDX1 ON IMPL_INTERFACE(NAME);
CREATE INDEX IIMPL_IDX2 ON IMPL_INTERFACE(CLASSID);

CREATE INDEX SUPER_IDX1 ON SUPERCLASS(NAME);
CREATE INDEX SUPER_IDX2 ON SUPERCLASS(CLASSID);

CREATE INDEX AUTH_IDX1 ON AUTHOR(NAME);

CREATE INDEX CLSAUTH_IDX1 ON CLASS_AUTHOR(AUTHORID);
CREATE INDEX CLSAUTH_IDX2 ON CLASS_AUTHOR(CLASSID);

CREATE INDEX MEMBER_IDX1 ON MEMBER(CLASSID);
CREATE INDEX MEMBER_IDX2 ON MEMBER(NAME);
CREATE INDEX MEMBER_IDX3 ON MEMBER(DOCID);
CREATE INDEX MEMBER_IDX4 ON MEMBER(ISSTATIC);
CREATE INDEX MEMBER_IDX5 ON MEMBER(ACCESSIBILITY);

CREATE INDEX FLD_IDX1 ON FIELD(TYPENAME);

CREATE INDEX METHOD_IDX1 ON METHOD(RETURNTYPENAME);

CREATE INDEX PARM_IDX1 ON PARAMETER(EXECMEMBERID);
CREATE INDEX PARM_IDX2 ON PARAMETER(TYPENAME);

CREATE INDEX THROW_IDX1 ON THROWNEXCEPTION(NAME);
CREATE INDEX THROW_IDX2 ON THROWNEXCEPTION(THROWERID);



--COMMIT;
