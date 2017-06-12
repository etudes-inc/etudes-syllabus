<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib uri="http://sakaiproject.org/jsf/sakai" prefix="sakai" %>
<%@ taglib uri="http://sakaiproject.org/jsf/syllabus" prefix="syllabus" %>
<% response.setContentType("text/html; charset=UTF-8"); %>
<f:view>

<jsp:useBean id="msgs" class="org.sakaiproject.util.ResourceLoader" scope="session">
   <jsp:setProperty name="msgs" property="baseName" value="org.sakaiproject.tool.syllabus.bundle.Messages"/>
</jsp:useBean>

	<sakai:view_container title="#{msgs.attachment}">
		<sakai:view_content>
			<h:form>				
				<h:panelGrid columns="1" styleClass="jsfFormTable" >
				  <h:column>
					<h:graphicImage url="/images/warning.gif" alt="" title="" height="16" width="16" style="border:0;margin-right:3px;"/>
			  		<h:outputText value="#{msgs.bar_delete_items}" /> 
			  		</h:column>
		  		</h:panelGrid>
				<div class="alertMessage">
					<h:outputText value="#{msgs.delAttConfAlert}" />
				</div>	
				<syllabus:syllabus_table value="#{SyllabusTool.prepareRemoveAttach}" var="eachAttach" summary="#{msgs.del_conf_listsummary}">
					<h:column>
						<f:facet name="header">
							<h:outputText value="#{msgs.title}" />
						</f:facet>
						<f:verbatim><h4></f:verbatim>
						<h:outputText value="#{eachAttach.name}"/>
						<f:verbatim></h4></f:verbatim>
						</h:column>
					<h:column>
						<f:facet name="header">
							<h:outputText value="#{msgs.size}" />
						</f:facet>
						<h:outputText value="#{eachAttach.size}"/>
					</h:column>
					<h:column>
						<f:facet name="header">
							<h:outputText value="#{msgs.type}" />
						</f:facet>
							<h:outputText value="#{eachAttach.type}"/>
						</h:column>
					<h:column>
						<f:facet name="header">
							<h:outputText value="#{msgs.created_by}" />
						</f:facet>
						<h:outputText value="#{eachAttach.createdBy}"/>
					</h:column>
					<h:column>
						<f:facet name="header">
							<h:outputText value="#{msgs.last_modified}" />
						</f:facet>
							<h:outputText value="#{eachAttach.lastModifiedBy}"/>
					</h:column>
				</syllabus:syllabus_table>
				<f:verbatim><p style="background: #EEEEEE;border: 0;margin: 5px 0 0 0;line-height: 2em;padding-left: 1em;"></f:verbatim>
					<h:commandButton 
					  value="#{msgs.bar_delete}" 
					  style="padding-left:2em;background: #eee url('images/delete.png') .2em no-repeat;"
					  action="#{SyllabusTool.processRemoveAttach}"
					  title="#{msgs.bar_delete} [Access key + l]"
					  accesskey="l" />
					<h:commandButton 
					  value="#{msgs.bar_cancel}" 
					  action="#{SyllabusTool.processRemoveAttachCancel}"
					  style="padding-left:2em;background: #eee url('images/cancel.gif') .2em no-repeat;"
					  title="#{msgs.bar_cancel} [Access key + x]"
					  accesskey="x" />
				<f:verbatim></p></f:verbatim>
			</h:form>
		</sakai:view_content>
	</sakai:view_container>
</f:view>
				
