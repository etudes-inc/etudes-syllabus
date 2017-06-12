CREATE TABLE `sakai_syllabus_track_view` (
  `lockId` int(11) NOT NULL default '0',
  `USER_ID` varchar(99) NOT NULL default '',
  `CONTEXT_ID` varchar(99) NOT NULL default '',
  `ACCEPTED_ON` datetime default NULL,
  `FIRST_VISIT` datetime default NULL,
  `LAST_VISIT` datetime default NULL,  
)

CREATE INDEX syllabus_track ON sakai_syllabus_track_view (context_id, user_id);
