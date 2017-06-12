/**********************************************************************************
 * $URL: https://source.etudes.org/svn/apps/syllabus/trunk/syllabus-impl/src/java/org/sakaiproject/component/app/syllabus/SyllabusManagerImpl.java $
 * $Id: SyllabusManagerImpl.java 5775 2013-08-30 21:14:53Z rashmim $
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
package org.sakaiproject.component.app.syllabus;

import java.util.Date;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.etudes.util.HtmlHelper;
import org.etudes.util.SqlHelper;
import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Expression;
import org.hibernate.exception.ConstraintViolationException;

import org.sakaiproject.api.app.syllabus.SyllabusAttachment;
import org.sakaiproject.api.app.syllabus.SyllabusData;
import org.sakaiproject.api.app.syllabus.SyllabusItem;
import org.sakaiproject.api.app.syllabus.SyllabusManager;
import org.sakaiproject.api.app.syllabus.SyllabusTrackView;
import org.sakaiproject.component.cover.ServerConfigurationService;
import org.sakaiproject.content.api.ContentHostingService;
import org.sakaiproject.content.api.ContentResource;
import org.sakaiproject.db.api.SqlReader;
import org.sakaiproject.db.api.SqlService;
import org.sakaiproject.user.cover.UserDirectoryService;
import org.sakaiproject.user.api.User;

import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;


/**
 * SyllabusManagerImpl provides convenience functions to query the database
 * 
 * @author <a href="mailto:jlannan@iupui.edu">Jarrod Lannan </a>
 * @version $Id:
 */
public class SyllabusManagerImpl extends HibernateDaoSupport implements SyllabusManager
{
  private ContentHostingService contentHostingService;
  private SqlService sqlService = null;

  private static final String QUERY_BY_USERID_AND_CONTEXTID = "findSyllabusItemByUserAndContextIds";
  private static final String QUERY_BY_CONTEXTID = "findSyllabusItemByContextId";
  private static final String QUERY_LARGEST_POSITION = "findLargestSyllabusPosition";
  private static final String USER_ID = "userId";
  private static final String CONTEXT_ID = "contextId";
  private static final String SURROGATE_KEY = "surrogateKey";
  private static final String SYLLABI = "syllabi";  
  private static final String FOREIGN_KEY = "foreignKey";
  private static final String QUERY_BY_SYLLABUSDATAID = "findSyllabusDataByDataIds";
  private static final String DATA_KEY = "syllabusId";
  private static final String SYLLABUS_DATA_ID = "syllabusId";
  private static final String ATTACHMENTS = "attachments";
  
  public void setContentHostingService(ContentHostingService contentHostingService) {
		this.contentHostingService = contentHostingService;
	}

	public void setSqlService(SqlService sqlService)
	{
		this.sqlService = sqlService;
	}

	public void init()
	{		
		if (ServerConfigurationService.getString("auto.ddl", "false").equals("true"))
		{
			sqlService.ddl(this.getClass().getClassLoader(), "syllabusTrackView");
		}
	}

	public void destroy()
	{
		//
	}

  /**
   * createSyllabusItem creates a new SyllabusItem
   * @param userId
   * @param contextId
   * @param redirectURL
   *        
   */
  public SyllabusItem createSyllabusItem(String userId, String contextId,
      String redirectURL)
  {
    if (userId == null || contextId == null)
    {
      throw new IllegalArgumentException("Null Argument");
    }
    else
    {
      // construct a new SyllabusItem
      SyllabusItem item = new SyllabusItemImpl(userId, contextId, redirectURL);      
      saveSyllabusItem(item);
      return item;
    }
  }
  
  /**
   * getSyllabiForSyllabusItem returns the collection of syllabi
   * @param syllabusItem
   */
  public Set getSyllabiForSyllabusItem(final SyllabusItem syllabusItem)
  {
    if (syllabusItem == null)
    {
      throw new IllegalArgumentException("Null Argument");
    }
    else
    {                 
      HibernateCallback hcb = new HibernateCallback()
      {                
        public Object doInHibernate(Session session) throws HibernateException,
            SQLException
        {            
          // get syllabi in an eager fetch mode
          Criteria crit = session.createCriteria(SyllabusItemImpl.class)
                      .add(Expression.eq(SURROGATE_KEY, syllabusItem.getSurrogateKey()))
                      .setFetchMode(SYLLABI, FetchMode.EAGER);
                      
          
          SyllabusItem syllabusItem = (SyllabusItem) crit.uniqueResult();
          
          if (syllabusItem != null){            
            return syllabusItem.getSyllabi();                                           
          }     
          return new TreeSet();
        }
      };             
      return (Set) getHibernateTemplate().execute(hcb);     
    }
  }  
  
  /**
   * createSyllabusData creates a persistent SyllabusData object
   * @param title
   * @param position
   * @param assetId
   * @param view
   * @param status
   * @param emailNotification 
   */
  public SyllabusData createSyllabusDataObject(String title, Integer position,
        String asset, String view, String status, String emailNotification)      
  {
    if (position == null)
    {
      throw new IllegalArgumentException("Null Argument");
    }
    else
    {
      // construct a new SyllabusData persistent object
      SyllabusData data = new SyllabusDataImpl();
      data.setTitle(title);
      data.setPosition(position);
      data.setAsset(HtmlHelper.clean(asset, true));
      data.setView(view);
      data.setStatus(status);
      data.setEmailNotification("none");
            
      saveSyllabus(data);
      return data;
    }
  }
    
  /**
   * removes a syllabus data object (on form cancel action) 
   * @see org.sakaiproject.api.app.syllabus.SyllabusManager#removeSyllabusDataObject(org.sakaiproject.api.app.syllabus.SyllabusData)
   */
  public void removeSyllabusDataObject(SyllabusData o)
  {
    getHibernateTemplate().delete(o);
  }
  
  /**
   * on reordering using drag drop , call this method.
   * @param newsyllabusPositions
   */
  public void sortSyllabusDataPositions(final ArrayList<SyllabusData> newsyllabusPositions)      
  {
    if (newsyllabusPositions == null )
    {
      throw new IllegalArgumentException("Null Argument");
    }
    else
    {
      HibernateCallback hcb = new HibernateCallback()
      {                
        public Object doInHibernate(Session session) throws HibernateException,
            SQLException
        {            
          // load objects from hibernate
        	int pos = 1;
        	for(SyllabusData sd : newsyllabusPositions)
        	{
        		 SyllabusData item =  (SyllabusData) session.get(SyllabusDataImpl.class, sd.getSyllabusId());
        		 item.setPosition(pos++);
        	}  
          
          return null;                    
        }
      };
      getHibernateTemplate().execute(hcb);
    }
  } 
  
  /**
   * On reordering through combo box, call this method.
   *  The items positions are not in order...on remove it doesn't adjust the positions...
   */
	public void moveSyllabusDataToNewPosition(final SyllabusItem syllabusItem, final SyllabusData d1, final SyllabusData d2)
	{
		   if (syllabusItem == null || d1 == null || d2 == null)
		    {
		      throw new IllegalArgumentException("Null Argument");
		    }
		    else
		    {
		      HibernateCallback hcb = new HibernateCallback()
		      {                
		        public Object doInHibernate(Session session) throws HibernateException,
		            SQLException
		        {            
		          // load objects from hibernate
		          SyllabusItem item = (SyllabusItem) session.get(SyllabusItemImpl.class, syllabusItem.getSurrogateKey());
		          SyllabusData moveOldPosition = (SyllabusData) session.get(SyllabusDataImpl.class, d1.getSyllabusId());
		          SyllabusData moveNewPosition = (SyllabusData) session.get(SyllabusDataImpl.class, d2.getSyllabusId());
		         
		          Set<SyllabusData> syllabiItems = item.getSyllabi();
		         Iterator<SyllabusData> iter= syllabiItems.iterator();

		         int o = moveOldPosition.getPosition().intValue();
		         int n = moveNewPosition.getPosition().intValue();
		         
		          if ( o == n || o == 0 || n == 0) return null;
		          
		          if (o < n)
		          {		 
		        	  int pos = o;
		        	  while(iter.hasNext())
		        	  {
		        		  
		        		  SyllabusData others = iter.next();
			        	  SyllabusData OtherData = (SyllabusData) session.get(SyllabusDataImpl.class, others.getSyllabusId());
			        	  int store = OtherData.getPosition().intValue();
			        	  if (store > o && store <= n)
			        	  {
			        	  OtherData.setPosition(pos);
			        	  pos = store;
			        	  }			        	  
		        	  }
		        	  moveOldPosition.setPosition(n);
		          }
		          else if (o > n)
		          {	
		        	  int pos = o;
		        	  SyllabusData[] items = syllabiItems.toArray(new SyllabusData[0]);
		        	  for(int i = items.length -1; i >=0 ; i--)
		        	  {		        		  
		        		  SyllabusData others = items[i];
			        	  SyllabusData OtherData = (SyllabusData) session.get(SyllabusDataImpl.class, others.getSyllabusId());
			        	  int store = OtherData.getPosition().intValue();
			        	  if (store < o && store >= n)
			        	  {
			        	  OtherData.setPosition(pos);
			        	  pos = store;
			        	  }			        	  
		        	  }
		        	  moveOldPosition.setPosition(n);
		          }
		          return null;                    
		        }
		      };
		      getHibernateTemplate().execute(hcb);
		    }	
		
	}
  
  /**
   * swapSyllabusDataPositions swaps positions for two SyllabusData objects
   * @param syllabusItem
   * @param d1
   * @param d2
   */
  public void swapSyllabusDataPositions(final SyllabusItem syllabusItem, final SyllabusData d1, final SyllabusData d2)      
  {
    if (syllabusItem == null || d1 == null || d2 == null)
    {
      throw new IllegalArgumentException("Null Argument");
    }
    else
    {
      HibernateCallback hcb = new HibernateCallback()
      {                
        public Object doInHibernate(Session session) throws HibernateException,
            SQLException
        {            
          // load objects from hibernate
          SyllabusItem item = (SyllabusItem) session.get(SyllabusItemImpl.class, syllabusItem.getSurrogateKey());
          SyllabusData data1 = (SyllabusData) session.get(SyllabusDataImpl.class, d1.getSyllabusId());
          SyllabusData data2 = (SyllabusData) session.get(SyllabusDataImpl.class, d2.getSyllabusId());
          
          Integer temp = data1.getPosition();
          data1.setPosition(data2.getPosition());
          data2.setPosition(temp);
          
          return null;                    
        }
      };
      getHibernateTemplate().execute(hcb);
    }
  }    


  /**
   * findLargestSyllabusPosition finds the largest syllabus data position for an item
   * @param syllabusItem
   */
  public Integer findLargestSyllabusPosition(final SyllabusItem syllabusItem)      
  {
    if (syllabusItem == null)
    {
      throw new IllegalArgumentException("Null Argument");
    }
    else
    {
      HibernateCallback hcb = new HibernateCallback()
      {                
        public Object doInHibernate(Session session) throws HibernateException,
            SQLException
        {            
          Query q = session.getNamedQuery(QUERY_LARGEST_POSITION);                
          q.setParameter(FOREIGN_KEY, syllabusItem.getSurrogateKey(), Hibernate.LONG);
          
          Integer position = (Integer) q.uniqueResult();
          
          if (position == null){
            return new Integer(0);
          }
          else{
            return position;
          }
          
        }
      };
      return (Integer) getHibernateTemplate().execute(hcb);
    }
  }    
  
    
  /**
   * getSyllabusItemByContextId finds a SyllabusItem
   * @param contextId
   * @return SyllabusItem
   *        
   */
  public SyllabusItem getSyllabusItemByContextId(final String contextId)
  {
    if (contextId == null)
    {
      throw new IllegalArgumentException("Null Argument");
    }
          
    HibernateCallback hcb = new HibernateCallback()
    {
      public Object doInHibernate(Session session) throws HibernateException,
          SQLException
      {
        Query q = session.getNamedQuery(QUERY_BY_CONTEXTID);                        
        q.setParameter(CONTEXT_ID, contextId, Hibernate.STRING);                   
        return q.uniqueResult();
      }
    };
        
    return (SyllabusItem) getHibernateTemplate().execute(hcb);
  }
  
  /**
   * getSyllabusItemByUserAndContextIds finds a SyllabusItem
   * @param userId
   * @param contextId
   * @return SyllabusItem
   *        
   */
  public SyllabusItem getSyllabusItemByUserAndContextIds(final String userId, final String contextId)
  {
    if (userId == null || contextId == null)
    {
      throw new IllegalArgumentException("Null Argument");
    }
          
    HibernateCallback hcb = new HibernateCallback()
    {
      public Object doInHibernate(Session session) throws HibernateException,
          SQLException
      {
        Query q = session.getNamedQuery(QUERY_BY_USERID_AND_CONTEXTID);                
        q.setParameter(USER_ID, userId, Hibernate.STRING);
        q.setParameter(CONTEXT_ID, contextId, Hibernate.STRING);                   
        return q.uniqueResult();
      }
    };
        
    return (SyllabusItem) getHibernateTemplate().execute(hcb);
  }
  
  /**
   * addSyllabusToSyllabusItem adds a SyllabusData object to syllabi collection
   * @param syllabusItem
   * @param syllabusData
   * @return Set
   */
  public void addSyllabusToSyllabusItem(final SyllabusItem syllabusItem, final SyllabusData syllabusData)
  {
             
    if (syllabusItem == null || syllabusData == null)
    {
      throw new IllegalArgumentException("Null Argument");
    }      
           
    HibernateCallback hcb = new HibernateCallback()
    {
      public Object doInHibernate(Session session) throws HibernateException,
          SQLException
      {

        SyllabusItem returnedItem = (SyllabusItem) session.get(SyllabusItemImpl.class, syllabusItem.getSurrogateKey());
        if (returnedItem != null){          
          returnedItem.getSyllabi().add(syllabusData);                   
          session.save(returnedItem);                              
        }           
        return null;
      }
    }; 
    getHibernateTemplate().execute(hcb);    
  }  
  
  
  /**
   * removeSyllabusToSyllabusItem loads many side of the relationship
   * @param syllabusItem
   * @param syllabusData
   * @return Set
   */
  public void removeSyllabusFromSyllabusItem(final SyllabusItem syllabusItem, final SyllabusData syllabusData)
  {
            
    if (syllabusItem == null || syllabusData == null)
    {
      throw new IllegalArgumentException("Null Argument");
    }      
           
    HibernateCallback hcb = new HibernateCallback()
    {
      public Object doInHibernate(Session session) throws HibernateException,
          SQLException
      {
        SyllabusItem returnedItem = (SyllabusItem) session.get(SyllabusItemImpl.class, syllabusItem.getSurrogateKey());
        if (returnedItem != null){                    
          returnedItem.getSyllabi().remove(syllabusData);          
          session.saveOrUpdate(returnedItem);          
        }           
        return null;
      }
    }; 
    getHibernateTemplate().execute(hcb);
  }  
  
  /**
   * saveSyllabusItem persists a SyllabusItem
   * @param item
   */
  public void saveSyllabusItem(SyllabusItem item)
  {
	  logger.debug("save syllabus surrogatekey" +item.getSurrogateKey() );	  
	  if(item.getSurrogateKey() != null && item.getSurrogateKey() != 0)
	  {
	  final SyllabusItem s1 = item;
	  
	  HibernateCallback hcb = new HibernateCallback()
	  {
		  public Object doInHibernate(Session session) throws HibernateException,
		  SQLException
		  {
			  SyllabusItem returnedItem = (SyllabusItem) session.get(SyllabusItemImpl.class, s1.getSurrogateKey());
			  if(returnedItem != null)
			  {
				  returnedItem.setRedirectURL(s1.getRedirectURL());
				  returnedItem.setOpenNewWindow(s1.isOpenNewWindow());
				  returnedItem.setSyllabi(s1.getSyllabi());
				  session.saveOrUpdate(returnedItem);
			  }
			  return null;
		  }
	  }; 
	  getHibernateTemplate().execute(hcb);
	  }
	  else getHibernateTemplate().save(item);
  }
  
  /**
   * saveSyllabus persists a SyllabusData object
   * @param item
   */
  public void saveSyllabus(SyllabusData data)
  {
    getHibernateTemplate().saveOrUpdate(data);
  }  

  public SyllabusData getSyllabusData(final String dataId)
  {
    if (dataId == null)
    {
      throw new IllegalArgumentException("Null Argument");
    }
    else
    {                 
      HibernateCallback hcb = new HibernateCallback()
      {
        public Object doInHibernate(Session session) throws HibernateException,
            SQLException
        {
          Long longObj = new Long(dataId);
          SyllabusData returnedData = (SyllabusData) session.get(SyllabusDataImpl.class, longObj);
          returnedData.setEmailNotification("none");
          return returnedData;
        }
      }; 
      return (SyllabusData) getHibernateTemplate().execute(hcb);
    }

  }  

  public SyllabusAttachment createSyllabusAttachmentObject(String attachId, String name)      
  {
    try
    {
      SyllabusAttachment attach = new SyllabusAttachmentImpl();
      
      attach.setAttachmentId(attachId);
      
      attach.setName(name);

      ContentResource cr = contentHostingService.getResource(attachId);
      attach.setSize((new Integer(cr.getContentLength())).toString());
      User creator = UserDirectoryService.getUser(cr.getProperties().getProperty(cr.getProperties().getNamePropCreator()));
      attach.setCreatedBy(creator.getDisplayName());
      User modifier = UserDirectoryService.getUser(cr.getProperties().getProperty(cr.getProperties().getNamePropModifiedBy()));
      attach.setLastModifiedBy(modifier.getDisplayName());
      attach.setType(cr.getContentType());
      String tempString = cr.getUrl();
      String newString = new String();
      char[] oneChar = new char[1];
      for(int i=0; i<tempString.length(); i++)
      {
        if(tempString.charAt(i) != ' ')
        {
          oneChar[0] = tempString.charAt(i);
          String concatString = new String(oneChar);
          newString = newString.concat(concatString);
        }
        else
        {
          newString = newString.concat("%20");
        }
      } 
      //tempString.replaceAll(" ", "%20");
      attach.setUrl(newString);

      saveSyllabusAttachment(attach);
      
      return attach;
    }
    catch(Exception e)
    {
      e.printStackTrace();
      return null;
    }
  }

  public void saveSyllabusAttachment(SyllabusAttachment attach)
  {
    getHibernateTemplate().saveOrUpdate(attach);
  }
  
  public void addSyllabusAttachToSyllabusData(final SyllabusData syllabusData, final SyllabusAttachment syllabusAttach)
  {
             
    if (syllabusData == null || syllabusAttach == null)
    {
      throw new IllegalArgumentException("Null Argument");
    }      
           
    HibernateCallback hcb = new HibernateCallback()
    {
      public Object doInHibernate(Session session) throws HibernateException,
          SQLException
      {
        SyllabusData returnedData = (SyllabusData) session.get(SyllabusDataImpl.class, syllabusData.getSyllabusId());
        if (returnedData != null){
          returnedData.getAttachments().add(syllabusAttach);
          session.save(returnedData);                              
        }           
        return null;
      }
    }; 
    getHibernateTemplate().execute(hcb);
  }  


  public void removeSyllabusAttachmentObject(SyllabusAttachment o)
  {
    getHibernateTemplate().delete(o);
  }
  
  public void removeSyllabusAttachSyllabusData(final SyllabusData syllabusData, final SyllabusAttachment syllabusAttach)
  {
            
    if (syllabusData == null || syllabusAttach == null)
    {
      throw new IllegalArgumentException("Null Argument");
    }      
           
    HibernateCallback hcb = new HibernateCallback()
    {
      public Object doInHibernate(Session session) throws HibernateException,
          SQLException
      {
        SyllabusData returnedData = (SyllabusData) session.get(SyllabusDataImpl.class, syllabusData.getSyllabusId());
        if (returnedData != null){                    
          returnedData.getAttachments().remove(syllabusAttach);          
          session.saveOrUpdate(returnedData);          
        }           
        return null;
      }
    }; 
    getHibernateTemplate().execute(hcb);
  }  

  public Set getSyllabusAttachmentsForSyllabusData(final SyllabusData syllabusData)
  {
    if (syllabusData == null)
    {
      throw new IllegalArgumentException("Null Argument");
    }
    else
    {                 
      HibernateCallback hcb = new HibernateCallback()
      {                
        public Object doInHibernate(Session session) throws HibernateException,
            SQLException
        {            
          Criteria crit = session.createCriteria(SyllabusDataImpl.class)
                      .add(Expression.eq(SYLLABUS_DATA_ID, syllabusData.getSyllabusId()))
                      .setFetchMode(ATTACHMENTS, FetchMode.EAGER);
                      
          
          SyllabusData syllabusData = (SyllabusData) crit.uniqueResult();
          
          if (syllabusData != null){            
            return syllabusData.getAttachments();                                           
          }     
          return new TreeSet();
        }
      };             
      return (Set) getHibernateTemplate().execute(hcb);     
    }
  }  

  public SyllabusAttachment getSyllabusAttachment(final String syllabusAttachId)
  {
    if (syllabusAttachId == null)
    {
      throw new IllegalArgumentException("Null Argument");
    }
    else
    {                 
      HibernateCallback hcb = new HibernateCallback()
      {
        public Object doInHibernate(Session session) throws HibernateException,
            SQLException
        {
          Long longObj = new Long(syllabusAttachId);
          SyllabusAttachment returnedAttach = (SyllabusAttachment) session.get(SyllabusAttachmentImpl.class, longObj);
          return returnedAttach;
        }
      }; 
      return (SyllabusAttachment) getHibernateTemplate().execute(hcb);
    }

  }
  
  /*
   * track user's acceptance
   * If student user has accepted the terms then it won't add again 
   * For instructors it updates the acceptance date.  
   */
  public void saveSyllabusAcceptance(final String contextId, final String userId)
	{
		if (userId == null || contextId == null)
		{
			throw new IllegalArgumentException("Null Argument");
		}
	
		SyllabusTrackViewImpl data = getSyllabusTrackViewByUserAndContextId(contextId, userId);

		if (data != null)
		{
			data.setAcceptedOn(new Date());
			data.setLastVisitAt(new Date());
			updateAcceptanceLastVisitRecord(data);
		}
		else
		{
			data = new SyllabusTrackViewImpl(userId, contextId, new Date(), new Date(), new Date());
			insertTrackRecord(data);
		}
	}

  /**
   * 
   * @param data
   */
  protected void insertTrackRecord(final SyllabusTrackViewImpl data)
	{		
		sqlService.transact(new Runnable()
		{
			public void run()
			{
				insertTrackRecordTx(data);
			}
		}, "insertTrackRecord: ");	
	}

  /**
   * 
   * @param data
   * @return
   */
	protected boolean insertTrackRecordTx(SyllabusTrackViewImpl data)
	{
		String sql = "INSERT INTO SAKAI_SYLLABUS_TRACK_VIEW(USER_ID, CONTEXT_ID, ACCEPTED_ON, FIRST_VISIT, LAST_VISIT) VALUES(?,?,?,?,?)";

		Object[] fields = new Object[5];
		int i = 0;
		fields[i++] = data.getUserId();
		fields[i++] = data.getContextId();
		fields[i++] = (data.getAcceptedOn() != null) ? new Timestamp(data.getAcceptedOn().getTime()) : null;
		fields[i++] = new Timestamp(data.getFirstVisitAt().getTime());
		fields[i++] = new Timestamp(data.getLastVisitAt().getTime());

		return sqlService.dbWrite(sql.toString(), fields);
	}  

	/**
	 * Update the last visit time.
	 * @param data
	 */
	protected void updateAcceptanceLastVisitRecord(final SyllabusTrackViewImpl data)
	{		
		sqlService.transact(new Runnable()
		{
			public void run()
			{
				updateAcceptanceLastVisitRecordTx(data);
			}
		}, "updateAcceptanceLastVisitRecord: ");	
	}

	/**
	 * The actual update statement to set last visit time and acceptance date
	 * @param data
	 * @return
	 */
	protected boolean updateAcceptanceLastVisitRecordTx(SyllabusTrackViewImpl data) {
		String sql = "UPDATE SAKAI_SYLLABUS_TRACK_VIEW SET LAST_VISIT = ?,ACCEPTED_ON = ? WHERE USER_ID=? AND CONTEXT_ID=?";

		Object[] fields = new Object[4];
		int i = 0;
		fields[i++] = new Timestamp(data.getLastVisitAt().getTime());
		fields[i++] = (data.getAcceptedOn() != null) ? new Timestamp(data.getAcceptedOn().getTime()) : null;
		fields[i++] = data.getUserId();
		fields[i++] = data.getContextId();		  

		return sqlService.dbWrite(sql.toString(), fields);
	}
		
	/*
	 * Returns the date when user accepted the terms
	 */
	public Date syllabusAcceptedOn(final String contextId, final String userId)
	{
		if (userId == null || contextId == null)
		{
			throw new IllegalArgumentException("Null Argument");
		}

		final List<Date> viewDate = new ArrayList<Date>();
		// Get all columns of the site visit table
		String sql = "SELECT ACCEPTED_ON FROM SAKAI_SYLLABUS_TRACK_VIEW WHERE USER_ID=? AND CONTEXT_ID=?";

		Object[] fields = new Object[2];
		fields[0] = userId;
		fields[1] = contextId;

		sqlService.dbRead(sql, fields, new SqlReader()
		{
			public Object readSqlResultRecord(ResultSet result)
			{
				try
				{
					Date vDate = result.getTimestamp("ACCEPTED_ON");
					viewDate.add(vDate);
					return null;
				}
				catch (SQLException e)
				{
					return null;
				}
			}
		});

		if (viewDate != null && viewDate.size() > 0) return viewDate.get(0);
		
		return null;
	}

	/*
	 * checks if user has accepted the syllabus terms returns true if user has accepted
	 */
	public boolean isSyllabusAcceptedByUserAndContextId(final String contextId, final String userId)
	{
		final List<Boolean> readFlag = new ArrayList<Boolean>();
		if (userId == null || contextId == null)
		{
			throw new IllegalArgumentException("Null Argument");
		}

		// Get all columns of the site visit table
		String sql = "SELECT ACCEPTED_ON FROM SAKAI_SYLLABUS_TRACK_VIEW WHERE USER_ID=? AND CONTEXT_ID=?";

		Object[] fields = new Object[2];
		fields[0] = userId;
		fields[1] = contextId;

		sqlService.dbRead(sql, fields, new SqlReader()
		{
			public Object readSqlResultRecord(ResultSet result)
			{
				try
				{
					Date vDate = result.getTimestamp("ACCEPTED_ON");
					if (vDate != null) readFlag.add(new Boolean(true));
					return null;
				}
				catch (SQLException e)
				{
					return null;
				}
			}
		});
		if (readFlag != null && readFlag.size() > 0) return readFlag.get(0).booleanValue();
		
		return false;
	}

	/**
	 *  Gets User tracking information for the syllabus. 
	 *  
	 *  @param contextId
	 *  	The site Id
	 *  @param userId
	 *  	The user Id
	 */
	protected SyllabusTrackViewImpl getSyllabusTrackViewByUserAndContextId(final String contextId, final String userId)
	{
		if (userId == null || contextId == null)
		{
			throw new IllegalArgumentException("Null Argument");
		}
		Connection dbConnection = null;
		SyllabusTrackViewImpl trackView = null;
		try
		{
			dbConnection = sqlService.borrowConnection();
			// Get all columns of the site visit table
			String sql = "SELECT USER_ID, CONTEXT_ID, ACCEPTED_ON, FIRST_VISIT, LAST_VISIT FROM SAKAI_SYLLABUS_TRACK_VIEW  WHERE USER_ID=? AND CONTEXT_ID=?";

			PreparedStatement pstmt = dbConnection.prepareStatement(sql);
			pstmt.setString(1, userId);
			pstmt.setString(2, contextId);
			ResultSet rs = pstmt.executeQuery();
			if (rs != null)
			{
				rs.next();
				String uId = rs.getString("USER_ID");
				String cId = rs.getString("CONTEXT_ID");
				java.sql.Timestamp aDate = rs.getTimestamp("ACCEPTED_ON");
				java.sql.Timestamp firstDate = rs.getTimestamp("FIRST_VISIT");
				java.sql.Timestamp lastDate = rs.getTimestamp("LAST_VISIT");
				trackView = new SyllabusTrackViewImpl(uId, cId, aDate, firstDate, lastDate);
				rs.close();
				pstmt.close();
			}
		}
		catch (Exception e)
		{

		}
		finally
		{
			try
			{
				if (dbConnection != null) sqlService.returnConnection(dbConnection);
			}
			catch (Exception e1)
			{

			}
		}

		return trackView;
	}
	
	/*
	 * Returns Map<userId, ViewedDate> for all users of a site. If the user has accepted Syllabus agreement then only he/she shows in the list.
	 */
	public Map getSyllabusUsersViewedDataByContextId(final String contextId)
	{
		final Map<String, Date> all = new HashMap<String, Date>();
		if (contextId == null)
		{
			throw new IllegalArgumentException("Null Argument");
		}

		// Get all columns of the site visit table
		String sql = "SELECT USER_ID, ACCEPTED_ON FROM SAKAI_SYLLABUS_TRACK_VIEW WHERE CONTEXT_ID=?";

		Object[] fields = new Object[1];
		fields[0] = contextId;

		sqlService.dbRead(sql, fields, new SqlReader()
		{
			public Object readSqlResultRecord(ResultSet result)
			{
				try
				{
					String userId = result.getString("USER_ID");
					Date viewDate = result.getTimestamp("ACCEPTED_ON");
					all.put(userId, viewDate);
					return null;
				}
				catch (SQLException e)
				{
					return null;
				}
			}
		});
		return all;
	}
  	
	/*
	 * Returns Map<userId, ViewedDate> for all users of a site. If the user has accepted Syllabus agreement then only he/she shows in the list.
	 */
	public Map getSyllabusUsersFirstViewedDataByContextId(final String contextId)
	{
		final Map<String, Date> all = new HashMap<String, Date>();
		if (contextId == null)
		{
			throw new IllegalArgumentException("Null Argument");
		}

		// Get all columns of the site visit table
		String sql = "SELECT USER_ID, FIRST_VISIT FROM SAKAI_SYLLABUS_TRACK_VIEW WHERE CONTEXT_ID=?";

		Object[] fields = new Object[1];
		fields[0] = contextId;

		sqlService.dbRead(sql, fields, new SqlReader()
		{
			public Object readSqlResultRecord(ResultSet result)
			{
				try
				{
					String userId = result.getString("USER_ID");
					Date viewDate = result.getTimestamp("FIRST_VISIT");
					all.put(userId, viewDate);
					return null;
				}
				catch (SQLException e)
				{
					return null;
				}
			}
		});
		return all;
	}
	
	/*
	 * Returns Map<userId, ViewedDate> for all users of a site. If the user has accepted Syllabus agreement then only he/she shows in the list.
	 */
	public Map getSyllabusUsersLastViewedDataByContextId(final String contextId)
	{
		final Map<String, Date> all = new HashMap<String, Date>();
		if (contextId == null)
		{
			throw new IllegalArgumentException("Null Argument");
		}

		// Get all columns of the site visit table
		String sql = "SELECT USER_ID, LAST_VISIT FROM SAKAI_SYLLABUS_TRACK_VIEW WHERE CONTEXT_ID=?";

		Object[] fields = new Object[1];
		fields[0] = contextId;

		sqlService.dbRead(sql, fields, new SqlReader()
		{
			public Object readSqlResultRecord(ResultSet result)
			{
				try
				{
					String userId = result.getString("USER_ID");
					Date viewDate = result.getTimestamp("LAST_VISIT");
					all.put(userId, viewDate);
					return null;
				}
				catch (SQLException e)
				{
					return null;
				}
			}
		});
		return all;
	}
	
	/**
	 * Track a user's visit
	 */
	public void trackSyllabusVisitsByUserId(final String contextId, final String userId)
	{
		if (userId == null || contextId == null)
		{
			throw new IllegalArgumentException("Null Argument");
		}
	
		SyllabusTrackViewImpl data  = getSyllabusTrackViewByUserAndContextId(contextId, userId);
		
		if (data != null )
		{			
			data.setLastVisitAt(new Date());
			updateAcceptanceLastVisitRecord(data);
		}
		else
		{
			data = new SyllabusTrackViewImpl(userId, contextId, null, new Date(), new Date());
			insertTrackRecord(data);
		}				
	}

  /*  public SyllabusAttachment creatSyllabusAttachmentResource(String attachId, String name)
  {
    SyllabusAttachment attach = new SyllabusAttachmentImpl();
    
    attach.setAttachmentId(attachId);
    
    attach.setName(name);
    
    return attach;
  }*/
}



