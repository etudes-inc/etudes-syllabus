/**********************************************************************************
 * $URL: https://source.etudes.org/svn/apps/syllabus/trunk/syllabus-app/src/java/org/sakaiproject/jsf/syllabus/SyllabusIfNotRender.java $
 * $Id: SyllabusIfNotRender.java 437 2010-02-11 19:48:43Z rashmim $
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
package org.sakaiproject.jsf.syllabus;

import java.io.IOException;
import java.util.Iterator;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.render.Renderer;

public class SyllabusIfNotRender extends Renderer
{
  public boolean supportsComponentType(UIComponent component)
  {
    return (component instanceof org.sakaiproject.jsf.syllabus.SyllabusIfNotComponent);
  }
  
  public void encodeBegin(FacesContext context, UIComponent component)
  throws IOException
  {
    ResponseWriter writer = context.getResponseWriter();
    
    String test = (String) component.getAttributes().get("test");
    if(test!=null)
      test = test.trim();
    
    if((test!=null) && (!test.equals("")))
    {
      writer.write("<div>");
    }
  }

  public void encodeEnd(FacesContext context, UIComponent component)
  throws IOException
  {
    ResponseWriter writer = context.getResponseWriter();
    
    String test = (String) component.getAttributes().get("test");
    if(test!=null)
      test = test.trim();

    if((test!=null) && (!test.equals("")))
    {
      writer.write("</div>");
    }  
  }
  
  public void encodeChildren(FacesContext context, UIComponent component)
  	throws IOException 
  {
    if (context == null || component == null) 
    {
      throw new NullPointerException();
    }

    String test = (String) component.getAttributes().get("test");
    if(test!=null)
      test = test.trim();
    
    if((test!=null) && (!test.equals("")))
    {
      Iterator kids = component.getChildren().iterator();
      
      while (kids.hasNext()) 
      {
        UIComponent kid = (UIComponent) kids.next();
        kid.encodeBegin(context);
        if (kid.getRendersChildren()) {
          kid.encodeChildren(context);
        }
        kid.encodeEnd(context);
      }
    }
    else
    {
    }
  }
}



