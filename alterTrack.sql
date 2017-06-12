alter table sakai_syllabus_track_view ADD COLUMN ACCEPTED_ON datetime default NULL;
alter table sakai_syllabus_track_view ADD COLUMN FIRST_VISIT datetime default NULL;
alter table sakai_syllabus_track_view ADD COLUMN LAST_VISIT datetime default NULL;

update sakai_syllabus_track_view set ACCEPTED_ON = VIEWED_ON where ACCEPTED_ON IS NULL AND VIEWED_ON IS NOT NULL;
update sakai_syllabus_track_view set LAST_VISIT = VIEWED_ON WHERE LAST_VISIT IS NULL AND VIEWED_ON IS NOT NULL;
