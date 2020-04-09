<%@ page import="java.io.File" %>
<%@ page import="com.fasterxml.jackson.dataformat.xml.XmlMapper" %>
<%@ page import="beans.StudentBean" %>
<%@ page import="html.HtmlGenerator" %>

<html>
    <head>
        <title>Students</title>
    </head>
    <body>
        <h1>Add new student</h1>
        <form action="./add-student" method="post">
            <label for="firstName">First Name: </label><input id="firstName" name="firstName" type="text"/><br/>
            <label for="lastName">Last Name: </label><input id="lastName" name="lastName" type="text"/><br/>
            <label for="age">Age: </label><input id="age" name="age" type="text"/><br/>
            <input type="submit" value="Add"/>
        </form>
        <hr/>
        <h1>Registered students</h1>
        <% File studentsXML = new File("D:\\students.xml");
        if (studentsXML.exists()) {
            XmlMapper mapper = new XmlMapper();

            StudentBean[] studentBeans = mapper.readValue(studentsXML, StudentBean[].class);
            for (StudentBean studentBean : studentBeans) {
                out.print(HtmlGenerator.toViewEntry(studentBean));
            }
        } else {
            out.print("<p>No students found</p>");
        } %>
    </body>
</html>