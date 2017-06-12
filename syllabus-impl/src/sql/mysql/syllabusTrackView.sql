--*********************************************************************************
-- $URL: https://source.etudes.org/svn/apps/syllabus/tags/1.0.5/syllabus-impl/impl/src/sql/mysql/syllabusTrackView.sql $
-- $Id: syllabusTrackView.sql 1264 2011-03-10 20:13:48Z rashmim $ 
--**********************************************************************************
--
-- Copyright (c) 2012 Etudes, Inc.
-- 
--
-- Licensed under the Apache License, Version 2.0 (the "License");
-- you may not use this file except in compliance with the License.
-- You may obtain a copy of the License at
-- 
--      http://www.apache.org/licenses/LICENSE-2.0
-- 
-- Unless required by applicable law or agreed to in writing, software
-- distributed under the License is distributed on an "AS IS" BASIS,
-- WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
-- See the License for the specific language governing permissions and
-- limitations under the License.
--
--*********************************************************************************/

CREATE TABLE  sakai_syllabus_track_view (
  `USER_ID` varchar(99) NOT NULL default '',
  `CONTEXT_ID` varchar(99) NOT NULL default '',
  `ACCEPTED_ON` datetime default NULL,
  `FIRST_VISIT` datetime default NULL,
  `LAST_VISIT` datetime default NULL,
   UNIQUE KEY `syllabus_track` (`CONTEXT_ID`,`USER_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
