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
		<div class="alertMessage"/>
			<%=msgs.getString("error_no_role")%>
		</div>
	</sakai:view_content>
	</sakai:view_container>
</f:view>