<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib uri="http://sakaiproject.org/jsf/sakai" prefix="sakai" %>
<%@ taglib uri="http://sakaiproject.org/jsf/syllabus" prefix="syllabus" %>

<% response.setContentType("text/html; charset=UTF-8"); %>

<f:view>
<sakai:view title="#{msgs.title_list}" toolCssHref="/e3/support/etudesDnd.css">
<jsp:useBean id="msgs" class="org.sakaiproject.util.ResourceLoader" scope="session">
   <jsp:setProperty name="msgs" property="baseName" value="org.sakaiproject.tool.syllabus.bundle.Messages"/>
</jsp:useBean>
	
	<style type="text/css">
	.actionCol {white-space: nowrap;}
	.headerTable {margin-top:5px;border-collapse: collapse;}
	.actionTable {margin-top:5px; border: 1px solid #EAEAEA;border-collapse: collapse;}
	.welcomeCol {height:30px;}
	.ListModCheckClass {vertical-align: top; width: 2%;}
	.ListSortClass {vertical-align: top; text-align:right; width: 7%;  white-space: nowrap;}
	.BottomImgSave {padding-left:2em;background: #eee url('images/save.png') .2em no-repeat;}	
	</style>
	
	<script language="JavaScript">
	// if redirected, just open in another window else
	// open with size approx what actual print out will look like
	function printFriendly(url) {
		if (url.indexOf("printFriendly") == -1) {
			window.open(url,"mywindow");
		}
		else {
			window.open(url,"mywindow","width=960,height=1100,scrollbars=yes"); 
		}
	}
	
	
	function selectAll()
	{
	  var listSizeVal = document.getElementById("syllEditForm:listSize").value;
	  var allcheckVal = document.getElementById("syllEditForm:syllItems:allcheck").checked;
	    	    	  
	    for (i=0;i<parseInt(listSizeVal);i++)
	    {	
		     if ( document.getElementById("syllEditForm:syllItems:"+i + ":check") != null ||  document.getElementById("syllEditForm:syllItems:"+i + ":check") != undefined)
		    {
				  if (allcheckVal == true)
				  	    document.getElementById("syllEditForm:syllItems:"+i + ":check").checked=true;	    
				  else 	document.getElementById("syllEditForm:syllItems:"+i + ":check").checked=false;
			} 	 
		}
	}
	
</script>

		<h:form id="syllEditForm">
		   <sakai:tool_bar>
		  <syllabus:syllabus_ifnot test="#{SyllabusTool.editAble}">
			  <%-- (gsilver) cannot pass a needed title attribute to these next items --%>
			   <h:commandLink styleClass="toolUiLink" id="previewAction" action="#{SyllabusTool.processStudentView}" immediate="true" >
			    <h:graphicImage url="/images/preview.png" alt="" title=""height="16" width="16"  style="border:0;margin-right:3px;"/>
		  		<h:outputText  value="#{msgs.bar_student_view}"/>
			 </h:commandLink>
			 <sakai:tool_bar_spacer/> 
		 
			<h:graphicImage url="/images/document_edit.gif" alt="" title="" height="16" width="16" style="border:0;margin-right:3px;" />
	  	    <h:outputText  value="#{msgs.bar_create_edit}"/>	
	  	   </syllabus:syllabus_ifnot>	   
	  </sakai:tool_bar>
	
		<!--author action bar -->
		 <h:panelGrid columns="1" border="0" width="100%" styleClass="headerTable"> 	 
			<h:column> 	
				<h:graphicImage url="/images/script.png" alt="" title="" height="16" width="16" style="border:0;margin-right:3px;"/>	    		    
				<h:outputText value="#{msgs.mainEditNotice}" />		
			 </h:column>
		 </h:panelGrid>
		
		  <h:panelGrid columns="1" border="1" width="100%" styleClass="actionTable"> 	
		  <h:column> 
			<h:panelGrid columns="3" columnClasses="actionCol" width="35%" border="0">	
					<h:column>
						<syllabus:syllabus_if test="#{SyllabusTool.syllabusItem.redirectURL}">
						<h:commandLink id="addAction" action="#{SyllabusTool.processListNew}" immediate="true" rendered="#{SyllabusTool.editAble == 'true'}" styleClass="toolUiLink">
						    <h:graphicImage url="/images/document_add.gif" alt="" title="" height="16" width="16" style="border:0;margin-right:3px;"/>
			  				<h:outputText  value="#{msgs.bar_new}"/>
				 		</h:commandLink>
				 		</syllabus:syllabus_if>
				   </h:column>
				   <h:column>	   		 
				   	<syllabus:syllabus_if test="#{SyllabusTool.syllabusItem.redirectURL}">
					   <h:commandLink id="redirectAction" action="#{SyllabusTool.processRedirect}" immediate="true" rendered="#{SyllabusTool.editAble == 'true'}" styleClass="toolUiLink">
					    	<h:graphicImage url="/images/link2me.png" alt="" title="" height="16" width="16" style="border:0;margin-right:3px;"/>
				  			<h:outputText  value="#{msgs.bar_redirect}"/>
					 	</h:commandLink>	
					 	</syllabus:syllabus_if>		
					 	<syllabus:syllabus_ifnot test="#{SyllabusTool.syllabusItem.redirectURL}">
						 	 <h:commandLink id="redirectEditAction" action="#{SyllabusTool.processRedirect}" immediate="true" rendered="#{SyllabusTool.editAble == 'true'}" styleClass="toolUiLink">
						    	<h:graphicImage url="/images/link2me.png" alt="" title="" height="16" width="16" style="border:0;margin-right:3px;"/>
					  			<h:outputText  value="#{msgs.bar_redirect_edit}"/>
						 	</h:commandLink>
					 	</syllabus:syllabus_ifnot>	 
					 </h:column>
				   <h:column>
				    <syllabus:syllabus_if test="#{SyllabusTool.syllabusItem.redirectURL}">
					 <h:commandLink id="delAction" action="#{SyllabusTool.processListDelete}" rendered="#{SyllabusTool.editAble == 'true'}" styleClass="toolUiLink">
				    	<h:graphicImage url="/images/delete.png" alt="" title="" height="16" width="16" style="border:0;margin-right:3px;"/>
			  			<h:outputText  value="#{msgs.update}"/>
				 	</h:commandLink>
				 	</syllabus:syllabus_if>
				 	<syllabus:syllabus_ifnot test="#{SyllabusTool.syllabusItem.redirectURL}">
				 	 <h:commandLink id="redirectDelAction" action="#{SyllabusTool.processUrlDelete}" rendered="#{SyllabusTool.editAble == 'true'}" styleClass="toolUiLink">
				    	<h:graphicImage url="/images/delete.png" alt="" title="" height="16" width="16" style="border:0;margin-right:3px;"/>
			  			<h:outputText  value="#{msgs.bar_redirect_delete}"/>
				 	</h:commandLink>
				 	</syllabus:syllabus_ifnot>
				 	</h:column>
				</h:panelGrid>	
				</h:column>
		   </h:panelGrid>	 

			<syllabus:syllabus_if test="#{SyllabusTool.syllabusItem.redirectURL}">				

				 <h:dataTable id="syllItems" cellspacing="10" cellpadding="10" value="#{SyllabusTool.entries}" var="eachEntry" border="0" columnClasses="ListModCheckClass,toolUiLink,,ListSortClass" summary="#{msgs.mainEditListSummary}" styleClass="listHier lines sortTableClass">
  
					<h:column rendered="#{! SyllabusTool.displayNoEntryMsg}">
							<f:facet name="header">
								 <h:selectBooleanCheckbox id="allcheck" onclick="selectAll()"/>
							</f:facet>
					<h:selectBooleanCheckbox id="check" value="#{eachEntry.selected}" title="#{msgs.selectThisCheckBox}"/>
					</h:column>		
					<h:column rendered="#{! SyllabusTool.displayNoEntryMsg}">
							<f:facet name="header">								
								<h:outputText value="#{msgs.mainEditHeaderTitle}" />
							</f:facet>						
							<f:verbatim><h4 class="specialLink"></f:verbatim>							
							<h:commandLink action="#{eachEntry.processListRead}" title="#{msgs.goToItem} #{eachEntry.entry.title}">
								<h:outputText value="#{eachEntry.entry.title}"/>
							</h:commandLink>
							<f:verbatim></h4></f:verbatim>
						</h:column>
						<h:column rendered="#{! SyllabusTool.displayNoEntryMsg}">
							<f:facet name="header">
								<h:outputText value="#{msgs.mainEditHeaderStatus}"/>
							</f:facet>
							
							<h:outputText value="#{eachEntry.entry.status}" rendered="#{eachEntry.entry.status != 'Draft'}" />
							<h:outputText value="#{eachEntry.entry.status}" style="color:#FF0000;" rendered="#{eachEntry.entry.status =='Draft'}" />
						</h:column>						
						<h:column rendered="#{! SyllabusTool.displayNoEntryMsg}">
							<f:facet name="header">
								<h:outputText value="" />
							</f:facet>					
							<h:outputText id="itemId" value="#{eachEntry.id}" styleClass="hide"/>			
							<h:panelGroup id="reorderGrp" styleClass="addDndImage"></h:panelGroup>														
						</h:column>
					</h:dataTable>
	  		<h:inputHidden id="listSize" value="#{SyllabusTool.entriesSize}"/>	  
	  		<h:outputText id="noItemsMsg" value="#{msgs.no_items_message}" rendered="#{SyllabusTool.displayNoEntryMsg}"/>		
			</syllabus:syllabus_if>
			<syllabus:syllabus_ifnot test="#{SyllabusTool.syllabusItem.redirectURL}">
				<h:outputText value="#{msgs.syllabus_url_alert}" rendered="#{!SyllabusTool.redirectUrlHttpsPrefix && !SyllabusTool.syllabusItem.openNewWindow}" styleClass="alertMessage" />
					
				<h:outputText value="<br/>" escape="false"/>
				<h:outputText value="Syllabus is currently linked to " />
				<h:outputLink value="#{SyllabusTool.redirectViewURL}" target="_blank" styleClass="toolUiLinkU">
					<h:outputText value="#{SyllabusTool.redirectViewURL}" escape="false"/>
				</h:outputLink>
			</syllabus:syllabus_ifnot>			
			<f:verbatim><div id="syllabusSaveDnd" style="background: #EEEEEE;border: 0;margin: 5px 0 0 0;line-height: 3em;padding-left: 1em;"></f:verbatim>
	  				<h:inputHidden id="newOrderToSend" value="#{SyllabusTool.newOrder}" rendered="#{SyllabusTool.showSave}" />
	  				<h:inputHidden id="oldPosition" value="#{SyllabusTool.oldPosition}" rendered="#{SyllabusTool.showSave}"  />	  	
	  				<h:inputHidden id="newPosition" value="#{SyllabusTool.newPosition}" rendered="#{SyllabusTool.showSave}" />
	  				<h:commandButton id="reOrderButton" value="Save" action="#{SyllabusTool.savePositions}" styleClass="BottomImgSave" rendered="#{SyllabusTool.showSave}"  />
	  				<h:commandButton id="reOrderSyllabusComboButton" value="Save Positions" action="#{SyllabusTool.saveToNewPositions}" styleClass="hide" rendered="#{SyllabusTool.showSave}" />			 					
	  		<f:verbatim></div></f:verbatim>		
		</h:form>		
	</sakai:view>
</f:view>
