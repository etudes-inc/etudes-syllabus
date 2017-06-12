<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib uri="http://sakaiproject.org/jsf/sakai" prefix="sakai" %>
<%@ taglib uri="http://sakaiproject.org/jsf/syllabus" prefix="syllabus" %>
<% response.setContentType("text/html; charset=UTF-8"); %>
<f:view>
<%@ page import="org.sakaiproject.tool.syllabus.SyllabusTool"%>
<%
javax.faces.context.FacesContext facesContext = javax.faces.context.FacesContext.getCurrentInstance();

SyllabusTool syllTool = (SyllabusTool)facesContext.getApplication().getVariableResolver().resolveVariable(facesContext, "SyllabusTool");
String status = syllTool.getSyllabusDataStatus();
boolean draft = true;
if (status != null && status.trim().equals("Posted")) draft = false;
String saveValue = "syllabusform:post";
if (draft) saveValue = "syllabusform:save";
%>
<jsp:useBean id="msgs" class="org.sakaiproject.util.ResourceLoader" scope="session">
   <jsp:setProperty name="msgs" property="baseName" value="org.sakaiproject.tool.syllabus.bundle.Messages"/>
</jsp:useBean>

	<sakai:view_container title="#{msgs.title_edit}">
		<sakai:view_content>
			<h:outputText value="#{SyllabusTool.alertMessage}" styleClass="alertMessage" rendered="#{SyllabusTool.alertMessage != null}" />
			<h:form id="syllabusform">
		  	 	<h:panelGrid columns="1" styleClass="jsfFormTable" >
				  <h:column>
					<h:graphicImage url="/images/document_edit.gif" alt="" title="" height="16" width="16" style="border:0;margin-right:3px;"/>
			  		<h:outputText value="#{msgs.editNotice}" /> 
			  		</h:column>
		  		</h:panelGrid>
				<sakai:doc_section>
					<h:outputText value="#{msgs.newSyllabusForm1}"/>
					<h:outputText value="*" styleClass="reqStarInline"/>
					<h:outputText value="#{msgs.newSyllabusForm2}"/>
				</sakai:doc_section>

			<h:panelGrid columns="1" styleClass="jsfFormTable" summary="layout">
				<h:panelGroup styleClass="shorttext required">
					<h:outputText value="*" styleClass="reqStar"/>
					<h:outputLabel for="title">
						<h:outputText value="#{msgs.syllabus_title}"/>
					</h:outputLabel>
					<h:inputText value="#{SyllabusTool.syllabusDataTitle}" id="title"/>
					<h:outputText value="#{msgs.empty_title_validate}" styleClass="alertMessage" 
									rendered="#{SyllabusTool.displayTitleErroMsg}"/>
				</h:panelGroup>
			</h:panelGrid>

			<p class="longtext">
				<label for=""> <%-- outputLabel needed here instead but there is no target to id --%>
					<h:outputText value="#{msgs.syllabus_content}"/>
				</label>		
				<sakai:inputRichText textareaOnly="false" rows="15" cols="90" id="syllabus_compose_read"  height="460" width="720" value="#{SyllabusTool.syllabusDataAsset}" collectionBase="#{SyllabusTool.collBase}" resourceId="#{SyllabusTool.resourceFile}"/>
			</p>	
			<div class="checkbox">
				<h:selectOneRadio value="#{SyllabusTool.syllabusDataView}"  layout="pageDirection" title="#{msgs.publicPrivate}">
					<f:selectItem itemValue="no" itemLabel="#{msgs.noPrivate}"/>
					<f:selectItem itemValue="yes" itemLabel="#{msgs.yesPublic}"/>
				</h:selectOneRadio>
			</div>
			<h4>
				<h:outputText value="#{msgs.attachment}"/>
			</h4>	
<%--						<h:outputText value="#{msgs.attachment}"/>
						<h:outputText value=" "/>--%>

<%--							<h:commandLink action="#{SyllabusTool.processAddAttWithOldItem}" onfocus="document.forms[0].onsubmit();">
									<h:outputText value="#{msgs.attachment_local} "/>
							</h:commandLink>--%>
<%--							<h:outputText value=" "/>
							<h:outputLink value="sakai.filepicker.helper/tool" onfocus="document.forms[0].onsubmit(); document.forms[0].submit();return false;">
							  <h:outputText id="file_picker" value=" #{msgs.file_picker}"/>
							</h:outputLink>--%>
										
<%-- (gsilver) cannot pass a needed title atribute to this next item --%>					
					<h:commandLink id="add_attachments_header" action="#{SyllabusTool.processAddAttachRedirect}" styleClass="toolUiLink" >
									<h:graphicImage id="addattach" value="images/attach.png" height="16" width="16" style="border:0;margin-right:3px;"/>
									<h:outputText value="#{msgs.add_attach}" />
					</h:commandLink>
							
					<h:outputText value="" styleClass="alertMessage"  rendered="#{SyllabusTool.displayEvilTagMsg}"/>
				    <h:outputText value="#{msgs.empty_content_validate} #{SyllabusTool.evilTagMsg}" styleClass="alertMessage"  rendered="#{SyllabusTool.displayEvilTagMsg}"/>

					<syllabus:syllabus_table value="#{SyllabusTool.allAttachments}" var="eachAttach" summary="#{msgs.edit_att_list_summary}" styleClass="listHier lines nolines">
					  <h:column rendered="#{!empty SyllabusTool.allAttachments}">
							<f:facet name="header">
								<h:outputText value="#{msgs.attachmentTitle}" />
							</f:facet>
							<f:verbatim><h5></f:verbatim>
							<sakai:contentTypeMap fileType="#{eachAttach.type}" mapType="image" var="imagePath" pathPrefix="/library/image/"/>									
							<h:graphicImage id="exampleFileIcon" value="#{imagePath}" />
							<h:outputText value="#{eachAttach.name}"/>
							<f:verbatim></h5></f:verbatim>
							<f:verbatim><div class="itemAction"></f:verbatim>

							<h:commandLink styleClass="toolUiLinkU" action="#{SyllabusTool.processDeleteAttach}" 
								onfocus="document.forms[0].onsubmit();" title="#{msgs.removeAttachmentLink} #{eachAttach.name}">
								<h:outputText value="#{msgs.mainEditHeaderRemove}"/>
								<f:param value="#{eachAttach.syllabusAttachId}" name="syllabus_current_attach"/>
							</h:commandLink>
							<f:verbatim></div></f:verbatim>
						</h:column>
					  <h:column rendered="#{!empty SyllabusTool.allAttachments}">
							<f:facet name="header">
								<h:outputText value="#{msgs.size}" />
							</f:facet>
							<h:outputText value="#{eachAttach.size}"/>
						</h:column>
					  <h:column rendered="#{!empty SyllabusTool.allAttachments}">
							<f:facet name="header">
		  			    <h:outputText value="#{msgs.type}" />
							</f:facet>
							<h:outputText value="#{eachAttach.type}"/>
						</h:column>
					  <h:column rendered="#{!empty SyllabusTool.allAttachments}">
							<f:facet name="header">
								<h:outputText value="#{msgs.created_by}" />
							</f:facet>
							<h:outputText value="#{eachAttach.createdBy}"/>
						</h:column>
					  <h:column rendered="#{!empty SyllabusTool.allAttachments}">
							<f:facet name="header">
								<h:outputText value="#{msgs.last_modified}" />
							</f:facet>
							<h:outputText value="#{eachAttach.lastModifiedBy}"/>
						</h:column>
					</syllabus:syllabus_table>					

				<f:verbatim><p style="background: #EEEEEE;border: 0;margin: 5px 0 0 0;line-height: 2em;padding-left: 1em;"></f:verbatim>
					<input type="hidden" id="saveVal" name="saveVal" value="<%=saveValue%>" />
					
					<h:commandButton id="post"
						action="#{SyllabusTool.processReadPost}"
						style="padding-left:2em;background: #eee url('images/return_sm.png') .2em no-repeat;"
						value="#{msgs.bar_post}" 
						accesskey="d"
						title="#{msgs.bar_post} [Access key + d]" />
					<h:commandButton
						action="#{SyllabusTool.processReadPreview}"
						value="#{msgs.bar_preview}"
						accesskey="v"
						style="padding-left:2em;background: #eee url('images/preview.png') .2em no-repeat;"
						title="#{msgs.bar_preview} [Access key + v]"	/>
					<h:commandButton id="save"
						action="#{SyllabusTool.processReadSave}"
						value="#{msgs.bar_save_draft}" 
						accesskey="s"
						style="padding-left:2em;background: #eee url('images/save_draft.png') .2em no-repeat;"
						title="#{msgs.bar_save_draft} [Access key + s]" />

					<h:commandButton
						action="#{SyllabusTool.processReadCancel}"
						value="#{msgs.cancel}" 
						accesskey="x"
						style="padding-left:2em;background: #eee url('images/cancel.gif') .2em no-repeat;"
						title="#{msgs.cancel} [Access key + x]" />
				<f:verbatim></p></f:verbatim>

			</h:form>
		</sakai:view_content>
	</sakai:view_container>
</f:view>
