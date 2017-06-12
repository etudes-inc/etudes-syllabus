/**********************************************************************************
 * $URL: https://source.etudes.org/svn/apps/syllabus/trunk/syllabus-hbm/src/java/org/sakaiproject/component/app/syllabus/SyllabusTrackViewImpl.java $
 * $Id: SyllabusTrackViewImpl.java 2968 2012-06-07 16:37:47Z rashmim $
 ***********************************************************************************
 *
 * Copyright (c) 2010, 2011, 2012 Etudes, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you
 * may not use this file except in compliance with the License. You may
 * obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 * implied. See the License for the specific language governing
 * permissions and limitations under the License.
 *
 **********************************************************************************/
package org.sakaiproject.component.app.syllabus;


import java.util.Date;
import org.sakaiproject.api.app.syllabus.SyllabusTrackView;

/**
 * Stores User's agreement to the terms of syllabus
 * 
 */

public class SyllabusTrackViewImpl implements SyllabusTrackView
{
  private String userId;
  private String contextId;
  private Integer lockId; // optimistic lock
  private Date acceptedOn;
  private Date firstVisitAt;
  private Date lastVisitAt;

  /**
   *  Public no-arg Constructor.
   */
  public SyllabusTrackViewImpl(){
    
    ;
  }
  
  /**
   * @param userId
   * @param contextId
   * @param viewedOn 
   */
  public SyllabusTrackViewImpl(String userId, String contextId, Date acceptedOn)
  {

    if (userId == null || contextId == null)
    {
      throw new IllegalArgumentException();
    }
    
    this.userId = userId;    
    this.contextId = contextId;
    this.acceptedOn = acceptedOn;
  }    

  public SyllabusTrackViewImpl(String userId, String contextId, Date acceptedOn, Date firstVisitAt, Date lastVisitAt)
  {

    if (userId == null || contextId == null)
    {
      throw new IllegalArgumentException();
    }
    
    this.userId = userId;    
    this.contextId = contextId;
    this.acceptedOn = acceptedOn;
    this.firstVisitAt = firstVisitAt;
    this.lastVisitAt = lastVisitAt;
  }  
  
  /**
   * @return Returns the contextId.
   */
  public String getContextId()
  {
    return contextId;
  }
 
  /**
   * @param contextId The contextId to set.
   */
  public void setContextId(String contextId)
  {
    this.contextId = contextId;
  }  
  
  /**
   * @return Returns the lockId.
   */
  public Integer getLockId()
  {
    return lockId;
  }
  
  /**
   * @param lockId The lockId to set.
   */
  public void setLockId(Integer lockId)
  {
    this.lockId = lockId;
  }

   /**
   * @return Returns the userId.
   */
  public String getUserId()
  {
    return userId;
  }
  
  /**
   * @param userId The userId to set.
   */
  public void setUserId(String userId)
  {
    this.userId = userId;
  }
  
	/**
	 * @return Returns the viewedOn.
	 */
	public Date getAcceptedOn()
	{
		return acceptedOn;
	}

	/**
	 * @param viewedOn
	 *        The viewedOn to set.
	 */
	public void setAcceptedOn(Date acceptedOn)
	{
		this.acceptedOn = acceptedOn;
	}

	/**
	 * @return Returns the firstVisitAt.
	 */
	public Date getFirstVisitAt()
	{
		return firstVisitAt;
	}

	/**
	 * @param firstVisitAt
	 *        The firstVisitAt to set.
	 */
	public void setFirstVisitAt(Date firstVisitAt)
	{
		this.firstVisitAt = firstVisitAt;
	}

	/**
	 *  @return Returns the lastVisitAt.
	 */
	public Date getLastVisitAt()
	{
		return lastVisitAt;
	}

	/**
	* @param lastVisitAt
	 *        The lastVisitAt to set.
	 */
	public void setLastVisitAt(Date lastVisitAt)
	{
		this.lastVisitAt = lastVisitAt;
	}

public boolean equals(Object obj)
  {
    if (this == obj) return true;
    if (!(obj instanceof SyllabusTrackViewImpl)) return false;
    SyllabusTrackViewImpl other = (SyllabusTrackViewImpl) obj;

    if ((userId == null ? other.userId == null : userId
        .equals(other.userId))        
        && (contextId == null ? other.contextId == null : contextId.equals(other.contextId))
        && (acceptedOn == null ? other.acceptedOn == null : acceptedOn.equals(other.acceptedOn)))
    {
      return true;
    }
    return false;
  }

  public int hashCode()
  {
    return userId.hashCode() + contextId.hashCode() +
    acceptedOn.hashCode();
  }

  public String toString()
  {
    StringBuffer sb = new StringBuffer();
    sb.append(", userId=");
    sb.append(userId);    
    sb.append(", contextId=");
    sb.append(contextId);
    sb.append(", acceptedOn=");
    sb.append(acceptedOn);  
    sb.append(", firstVisit=");
    sb.append(firstVisitAt); 
    sb.append(", lastVisit=");
    sb.append(lastVisitAt); 
    sb.append(", lockId=");
    sb.append(lockId);
    sb.append("}");
    return sb.toString();
  }   
}
