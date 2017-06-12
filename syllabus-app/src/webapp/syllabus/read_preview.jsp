<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib uri="http://sakaiproject.org/jsf/sakai" prefix="sakai" %>
<%@ taglib uri="http://sakaiproject.org/jsf/syllabus" prefix="syllabus" %>
<% response.setContentType("text/html; charset=UTF-8"); %>
<f:view>

<jsp:useBean id="msgs" class="org.sakaiproject.util.ResourceLoader" scope="session">
   <jsp:setProperty name="msgs" property="baseName" value="org.sakaiproject.tool.syllabus.bundle.Messages"/>
</jsp:useBean>

	<sakai:view_container>
		<sakai:view_content>
			<h:outputText value="#{SyllabusTool.alertMessage}" styleClass="alertMessage" rendered="#{SyllabusTool.alertMessage != null}" />
			<h:form>
			 	<h:panelGrid columns="1" styleClass="jsfFormTable" >
				  <h:column>
					<h:graphicImage url="/images/preview.png" alt="" title="" height="16" width="16" style="border:0;margin-right:3px;"/>
			  		<h:outputText value="#{msgs.previewNotice}" /> 
			  		</h:column>
		  		</h:panelGrid>
			<h4>
				<h:outputText value="#{SyllabusTool.syllabusDataTitle}" />
			</h4>
			<div class="indnt1">
				<syllabus:syllabus_htmlShowArea value="#{SyllabusTool.syllabusDataAsset}" />
			</div>	

			<h:dataTable value="#{SyllabusTool.allAttachments}" var="eachAttach" styleClass="indnt1" summary="">
			 	<h:column>
					<f:facet name="header">
						<h:outputText value="" />
						</f:facet>
					<sakai:contentTypeMap fileType="#{eachAttach.type}" mapType="image" var="imagePath" pathPrefix="/library/image/"/>									
					<h:graphicImage id="exampleFileIcon" value="#{imagePath}" />
					<h:outputLink value="#{eachAttach.theUrl}" target="_blank" title="#{msgs.openLinkNewWindow}">
						<h:outputText value=" " /><h:outputText value="#{eachAttach.name}" />
					</h:outputLink>
				</h:column>
			</h:dataTable>				
				<%-- (gsilver) cannot pass a needed title atribute to this next item --%>
				<f:verbatim><p style="background: #EEEEEE;border: 0;margin: 5px 0 0 0;line-height: 2em;padding-left: 1em;"></f:verbatim>
					<h:commandButton
						action="#{SyllabusTool.processReadPreviewBack}"
						style="padding-left:2em;background: #eee url('images/return_sm.png') .2em no-repeat;"
						value="#{msgs.revise}" 
						accesskey="r"
						title="#{msgs.revise} [Access key + r]" />
				<f:verbatim></p></f:verbatim>

			</h:form>
		</sakai:view_content>
	</sakai:view_container>
</f:view>
