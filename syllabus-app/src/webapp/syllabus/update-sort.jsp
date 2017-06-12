<%@ page import="org.sakaiproject.tool.syllabus.SyllabusTool"%>

<% 
final javax.faces.context.FacesContext facesContext = javax.faces.context.FacesContext.getCurrentInstance();
final SyllabusTool syllabusTool = (SyllabusTool)facesContext.getApplication().getVariableResolver().resolveVariable(facesContext, "SyllabusTool");

System.out.println("in the update page");
String syllabusOrder = request.getParameter("syllabusOrder");	
System.out.println("Syllabus order:" + syllabusOrder);
syllabusTool.prepareForSort(syllabusOrder);
%>