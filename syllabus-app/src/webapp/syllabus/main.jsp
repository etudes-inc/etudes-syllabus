<%@ page import="org.sakaiproject.tool.syllabus.SyllabusTool"%>
<html>
<head>
<title>Syllabus</title>
</head>
<body>
<%

final javax.faces.context.FacesContext facesContext = javax.faces.context.FacesContext.getCurrentInstance();

final SyllabusTool syllabusTool = (SyllabusTool)facesContext.getApplication().getVariableResolver().resolveVariable(facesContext, "SyllabusTool");
syllabusTool.setShowAgreement(null);
syllabusTool.setDisplayBlockMsg(null);
syllabusTool.setMapBlocked(null);

String allowOrnot = syllabusTool.checkUser();	
String gopage = "noAccess.jsf";
if(allowOrnot != null && allowOrnot.equals("true"))
 {
	allowOrnot = syllabusTool.getEditAble();	
	if(allowOrnot != null && allowOrnot.equals("true"))
		gopage = "main_edit.jsf";
	else  gopage = "main_view.jsf";	
 }
 String backAddr = request.getParameter("from");
 syllabusTool.setReturnAddr(backAddr);
%>
<br />
<jsp:forward page="<%=gopage%>"/>
</body>
</html> 
