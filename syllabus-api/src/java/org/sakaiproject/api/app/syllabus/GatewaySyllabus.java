/**********************************************************************************
 * $URL: https://source.etudes.org/svn/apps/syllabus/trunk/syllabus-api/src/java/org/sakaiproject/api/app/syllabus/GatewaySyllabus.java $
 * $Id: GatewaySyllabus.java 437 2010-02-11 19:48:43Z rashmim $
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

/**
 * @author <a href="mailto:cwen.iupui.edu">Chen Wen</a>
 * @version $Id: GatewaySyllabus.java 437 2010-02-11 19:48:43Z rashmim $
 * 
 */
public interface GatewaySyllabus
{
  public ArrayList getAttachList();

  public void setAttachList(ArrayList attachList);

  public SyllabusData getSyllabusData();

  public void setSyllabusData(SyllabusData syllabusData);
}