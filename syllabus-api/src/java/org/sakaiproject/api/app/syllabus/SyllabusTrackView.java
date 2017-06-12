/**********************************************************************************
 * $URL: https://source.etudes.org/svn/apps/syllabus/trunk/syllabus-api/src/java/org/sakaiproject/api/app/syllabus/SyllabusTrackView.java $
 * $Id: SyllabusTrackView.java 2968 2012-06-07 16:37:47Z rashmim $
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
package org.sakaiproject.api.app.syllabus;

import java.util.Date;

public interface SyllabusTrackView {

	/**
	 * @return Returns the contextId.
	 */
	public abstract String getContextId();

	/**
	 * @param contextId The contextId to set.
	 */
	public abstract void setContextId(String contextId);

	/**
	 * @return Returns the lockId.
	 */
	public abstract Integer getLockId();

	/**
	 * @param lockId The lockId to set.
	 */
	public abstract void setLockId(Integer lockId);
	
	/**
	 * @return Returns the userId.
	 */
	public abstract String getUserId();

	/**
	 * @param userId The userId to set.
	 */
	public abstract void setUserId(String userId);

	/**
	 * @return Returns the viewedOn.
	 */
	public abstract Date getAcceptedOn();

	/**
	 * @param viewedOn The viewedOn to set.
	 */
	public abstract void setAcceptedOn(Date acceptedOn);

	/**
	 * @return Returns the firstVisitAt.
	 */
	public abstract Date getFirstVisitAt();

	/**
	 * @param viewedOn The firstVisitAt to set.
	 */
	public abstract void setFirstVisitAt(Date firstVisitAt);
	
	/**
	 * @return Returns the lastVisitAt.
	 */
	public abstract Date getLastVisitAt();

	/**
	 * @param lastVisitAt The lastVisitAt to set.
	 */
	public abstract void setLastVisitAt(Date lastVisitAt);
}
