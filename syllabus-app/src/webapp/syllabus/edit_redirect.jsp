<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib uri="http://sakaiproject.org/jsf/sakai" prefix="sakai" %>
<% response.setContentType("text/html; charset=UTF-8"); %>
<f:view>
<jsp:useBean id="msgs" class="org.sakaiproject.util.ResourceLoader" scope="session">
   <jsp:setProperty name="msgs" property="baseName" value="org.sakaiproject.tool.syllabus.bundle.Messages"/>
</jsp:useBean>

<style type="text/css">
	.actionTable {border: 1px solid #EAEAEA;border-collapse: collapse; width:95%;}
	.actionTable th{ background-color: #EAEAEA;}
</style>
	
	<sakai:view_container title="#{msgs.title_edit}">
		<sakai:view_content>
			<h:form>
			<h:panelGrid columns="1" styleClass="jsfFormTable" >
				  <h:column>
					<h:graphicImage url="/images/link2me.png" alt="#{msgs.bar_redirect}" title="#{msgs.bar_redirect}" height="16" width="16" style="border:0;margin-right:3px;"/>
					<h:outputText value="#{msgs.bar_redirect}" style="margin-top:5px;margin-bottom:5px;" />
				</h:column>
				</h:panelGrid>
				<h:outputText value="<br/>" escape="false"/>	
				<h:outputText value="#{msgs.syllabus_desc}" style="margin-top:5px;margin-bottom:5px;" />	
				<h:outputText value="<br/>" escape="false"/>		
				<h:panelGrid styleClass="actionTable" columns="1" cellpadding="3px" >
						<h:column>							
						<h:panelGrid styleClass="listHier" columns="2" summary="layout">
							<h:column>								
								<h:outputText value="*" style="font-color:red"/>
								<h:outputLabel for="urlValue"><h:outputText value="#{msgs.syllabus_url}"/></h:outputLabel>
							</h:column>
							<h:column>							
								<h:inputText id="urlValue" value="#{SyllabusTool.currentRediredUrl}" size="65" />
							</h:column>
							<h:column/>
							<h:column>
								<h:selectBooleanCheckbox id="newWindow" value="#{SyllabusTool.openNewWindow}" />
								<h:outputText value="#{msgs.syllabus_url_newWindow}"/>
							</h:column>
						</h:panelGrid>
					</h:column>
					</h:panelGrid>
					
				<f:verbatim><p style="background: #EEEEEE;border: 0;margin: 5px 0 0 0;line-height: 2em;padding-left: 1em;"></f:verbatim>
					<h:commandButton
						style="padding-left:2em;background: #eee url('images/save.png') .2em no-repeat;"
						action="#{SyllabusTool.processEditSaveRedirect}"
						value="#{msgs.save}" 
						title="#{msgs.save} [Access key + s]" 
						accesskey="s" />
				
				   	 <h:commandButton
						style="padding-left:2em;background: #eee url('images/delete.png') .2em no-repeat;"
						action="#{SyllabusTool.processUrlDelete}" 
						value="#{msgs.update}" 
						title="#{msgs.update} [Access key + d]" 
						accesskey="d" />
						
					<h:commandButton
						style="padding-left:2em;background: #eee url('images/cancel.gif') .2em no-repeat;"
						action="#{SyllabusTool.processEditCancelRedirect}"
						value="#{msgs.cancel}" 
						title="#{msgs.cancel} [Access key + x]" 
						accesskey="x" />
				<f:verbatim></p></f:verbatim>

			</h:form>
		</sakai:view_content>
	</sakai:view_container>
</f:view>
