/**********************************************************************************
 * $URL: https://source.etudes.org/svn/apps/syllabus/trunk/syllabus-app/src/java/org/sakaiproject/jsf/syllabus/SyllabusIframeTag.java $
 * $Id: SyllabusIframeTag.java 437 2010-02-11 19:48:43Z rashmim $
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

import javax.faces.component.UIComponent;
import javax.faces.el.ValueBinding;
import javax.faces.webapp.UIComponentTag;
import javax.faces.application.Application;
import javax.faces.context.FacesContext;

public class SyllabusIframeTag extends UIComponentTag
{
  private String redirectUrl;
  
  private String width;
  
  private String height;
  
  public void setRedirectUrl(String redirectUrl)
  {
    this.redirectUrl = redirectUrl;
  }
  
  public String getedirectUrl()
  {
    return redirectUrl;
  }
  
  public void setWidth(String width)
  {
    this.width = width;
  }
  
  public String getWidth()
  {
    return width;
  }
  
  public void setHeight(String height)
  {
    this.height = height;
  }
  
  public String getHeight()
  {
    return height;
  }
  
  public String getComponentType()
  {
    return "SakaiSyllabusIframe";
  }
  
  public String getRendererType()
  {
    return "SakaiSyllabusIframeRender";
  }
  
  protected void setProperties(UIComponent component)
  {
    super.setProperties(component);
    setString(component, "redirectUrl", redirectUrl);
    setString(component, "width", width);
    setString(component, "height", height);
  }
  
  public void release()
  {
    super.release();
    
    redirectUrl = null;
    width = null;
    height = null;
  }
  
  public static void setString(UIComponent component, String attributeName,
      String attributeValue)
  {
    if (attributeValue == null) return;
    if (UIComponentTag.isValueReference(attributeValue)) setValueBinding(
        component, attributeName, attributeValue);
    else
      component.getAttributes().put(attributeName, attributeValue);
  }
  
  public static void setValueBinding(UIComponent component,
      String attributeName, String attributeValue)
  {
    FacesContext context = FacesContext.getCurrentInstance();
    Application app = context.getApplication();
    ValueBinding vb = app.createValueBinding(attributeValue);
    component.setValueBinding(attributeName, vb);
  }
}



