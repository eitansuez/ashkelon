--
-- optimizing tables
-- date: 3/22/2001
-- by eitan suez
-- copyright uptodata inc 2001
--

-- template:
--select max(length($)) from ;
--

select max(length(summarydescription)) from doc;
select max(length(description)) from doc;
select max(length(deprecated)) from doc;
select max(length(since)) from doc;

select max(length(label)) from reference;
select max(length(refdoc_name)) from reference;

select max(length(name)) from package;

select max(length(name)) from classtype;
select max(length(qualifiedname)) from classtype;
select max(length(superclassname)) from classtype;
select max(length(version)) from classtype;
select max(length(modifier)) from classtype;

select max(length(name)) from impl_interface;

select max(length(name)) from author;

select max(length(name)) from member;
select max(length(qualifiedname)) from member;
select max(length(modifier)) from member;

select max(length(typename)) from field;

select max(length(signature)) from execmember;
select max(length(fullyqualifiedname)) from execmember;

select max(length(returntypename)) from method;
select max(length(returndescription)) from method;

select max(length(name)) from parameter;
select max(length(description)) from parameter;
select max(length(typename)) from parameter;

select max(length(name)) from thrownexception;
select max(length(description)) from thrownexception;

select max(length(seqname)) from seqs;

