<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib uri="http://sakaiproject.org/jsf/sakai" prefix="sakai" %>
<% response.setContentType("text/html; charset=UTF-8"); %>
<f:view>

<jsp:useBean id="msgs" class="org.sakaiproject.util.ResourceLoader" scope="session">
   <jsp:setProperty name="msgs" property="baseName" value="org.sakaiproject.tool.syllabus.bundle.Messages"/>
</jsp:useBean>

	<sakai:view_container title="#{msgs.title_list}">
		<sakai:view_content>
			<h:form>
		  	
			<h:panelGrid columns="1" styleClass="jsfFormTable" >
				  <h:column>
					<h:graphicImage url="/images/warning.gif" alt="" title="" height="16" width="16" style="border:0;margin-right:3px;"/>
			  		<h:outputText value="#{msgs.delConfNotice}" /> 
			  		</h:column>
		  	</h:panelGrid>	
			<div class="alertMessage">
				<h:outputText  value="#{msgs.delConfAlert}" />
			</div>
				<sakai:flat_list value="#{SyllabusTool.selectedEntries}" var="eachEntry"  summary="#{msgs.del_conf_listsummary}" styleClass="listHier lines nolines">
					<h:column>
						<f:facet name="header">                                   
							<h:outputText  value="#{msgs.delConfHeaderItem}" />
						</f:facet>
						<h:outputText value="#{eachEntry.entry.title}"/>
					</h:column>
					<h:column>
						<f:facet name="header">
							<h:outputText value="#{msgs.delConfHeaderStatus}"/>
						</f:facet>
						<h:outputText value="#{eachEntry.entry.status}"/>
					</h:column>
					<h:column>
						<f:facet name="header">
							<h:outputText value="#{msgs.delConfHeaderPublicView}" />
						</f:facet>
						<h:outputText value="#{eachEntry.entry.view}"/>
					</h:column>
				</sakai:flat_list>
				<f:verbatim><p style="background: #EEEEEE;border: 0;margin: 5px 0 0 0;line-height: 2em;padding-left: 1em;"></f:verbatim>
					<h:commandButton
						action="#{SyllabusTool.processDelete}"
						style="padding-left:2em;background: #eee url('images/delete.png') .2em no-repeat;"
						value="#{msgs.title_delete} "
						title="#{msgs.title_delete} [Access key + l]"
						accesskey="l" />
					<h:commandButton
						action="#{SyllabusTool.processDeleteCancel}"
						value="#{msgs.cancel}"
						title="#{msgs.cancel} [Access key + x]"
						style="padding-left:2em;background: #eee url('images/cancel.gif') .2em no-repeat;"
						accesskey="x" />
				<f:verbatim></p></f:verbatim>	
			</h:form>
		</sakai:view_content>
	</sakai:view_container>
</f:view>
				
