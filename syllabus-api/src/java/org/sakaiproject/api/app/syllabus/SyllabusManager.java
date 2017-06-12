/**********************************************************************************
 * $URL: https://source.etudes.org/svn/apps/syllabus/trunk/syllabus-api/src/java/org/sakaiproject/api/app/syllabus/SyllabusManager.java $
 * $Id: SyllabusManager.java 4858 2013-05-15 23:07:30Z rashmim $
 ***********************************************************************************
 *
 * Copyright (c) 2003, 2004, 2005, 2006 The Sakai Foundation.
 * 
 * Licensed under the Educational Community License, Version 1.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at
 * 
 *      http://www.opensource.org/licenses/ecl1.php
 * 
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an "AS IS" BASIS, 
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. 
 * See the License for the specific language governing permissions and 
 * limitations under the License.
 *
 **********************************************************************************/
package org.sakaiproject.api.app.syllabus;

import java.util.ArrayList;
import java.util.Date;
import java.util.Map;
import java.util.Set;

public interface SyllabusManager
{
  /**
   * creates an SyllabusItem
   */
  public SyllabusItem createSyllabusItem(String userId, String contextId,
      String redirectURL);

  public SyllabusItem getSyllabusItemByUserAndContextIds(final String userId,
      final String contextId);

  public void saveSyllabusItem(SyllabusItem item);
  
  public void addSyllabusToSyllabusItem(final SyllabusItem syllabusItem, final SyllabusData syllabusData);
  
  public void removeSyllabusFromSyllabusItem(final SyllabusItem syllabusItem, final SyllabusData syllabusData);
  
  public SyllabusData createSyllabusDataObject(String title, Integer position,
      String assetId, String view, String status, String emailNotification);
  
  public void removeSyllabusDataObject(SyllabusData o);
  
  public Set getSyllabiForSyllabusItem(final SyllabusItem syllabusItem);
  
  public void swapSyllabusDataPositions(final SyllabusItem syllabusItem, final SyllabusData d1, final SyllabusData d2);
  
  public void saveSyllabus(SyllabusData data);
  
  public Integer findLargestSyllabusPosition(final SyllabusItem syllabusItem);
  
  public SyllabusItem getSyllabusItemByContextId(final String contextId);
  
  public SyllabusData getSyllabusData(final String dataId);
  
  public SyllabusAttachment createSyllabusAttachmentObject(String attachId, String name);      

  public void saveSyllabusAttachment(SyllabusAttachment attach);

  public void addSyllabusAttachToSyllabusData(final SyllabusData syllabusData, final SyllabusAttachment syllabusAttach);

  public void removeSyllabusAttachmentObject(SyllabusAttachment o);

  public void removeSyllabusAttachSyllabusData(final SyllabusData syllabusData, final SyllabusAttachment syllabusAttach);

  public Set getSyllabusAttachmentsForSyllabusData(final SyllabusData syllabusData);

  public SyllabusAttachment getSyllabusAttachment(final String syllabusAttachId);
  
  // track user's acceptance
  public void saveSyllabusAcceptance(String contextId, String userId);
  
  // for a-meter and coursemap
  public Date syllabusAcceptedOn(final String contextId, final String userId);
  
  // Is syllabus read and accepted by user
  public boolean isSyllabusAcceptedByUserAndContextId(final String contextId, final String userId);
  
  // get all users accept date for the syllabus
  public Map<String, Date> getSyllabusUsersViewedDataByContextId(final String contextId);
  
  // track user's visit to a syllabus
  public void trackSyllabusVisitsByUserId(final String contextId, final String userId);
  
  // get user's first visit date
  public Map<String, Date> getSyllabusUsersFirstViewedDataByContextId(final String contextId);
  
  // get all users last visit date
  public Map<String, Date> getSyllabusUsersLastViewedDataByContextId(final String contextId);
  
  // Reordering using drag drop 
  public void sortSyllabusDataPositions(final ArrayList<SyllabusData> newsyllabusPositions);
  
  // Reordering using combo box component
  public void moveSyllabusDataToNewPosition(final SyllabusItem syllabusItem, final SyllabusData d1, final SyllabusData d2);
  
  //public SyllabusAttachment creatSyllabusAttachmentResource(String attachId, String name);
}
