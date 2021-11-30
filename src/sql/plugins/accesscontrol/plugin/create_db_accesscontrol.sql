
--
-- Structure for table accesscontrol_accesscontrol
--

DROP TABLE IF EXISTS accesscontrol_accesscontrol;
CREATE TABLE accesscontrol_accesscontrol (
	id_access_control int AUTO_INCREMENT,
	name varchar(255) default '',
	description long varchar,
	creation_date date NOT NULL DEFAULT CURRENT_TIMESTAMP,
	is_enabled SMALLINT NOT NULL DEFAULT 0,
	workgroup_key varchar(255) default '',
	PRIMARY KEY (id_access_control)
);

--
-- Structure for table accesscontrol_accesscontroller
--

DROP TABLE IF EXISTS accesscontrol_accesscontroller;
CREATE TABLE accesscontrol_accesscontroller (
	id_access_controller int AUTO_INCREMENT,
	type varchar(255),
	id_access_control int,
	bool_cond varchar(50),
	display_order int,
	PRIMARY KEY (id_access_controller)
);
CREATE INDEX index_ac_accontroller_id ON accesscontrol_accesscontroller ( id_access_control );