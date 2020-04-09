<%@ page import="java.io.File" %>
<%@ page import="com.fasterxml.jackson.dataformat.xml.XmlMapper" %>
<%@ page import="beans.StudentBean" %>
<%@ page import="java.util.Arrays" %>
<%@ page import="java.util.stream.Stream" %>
<%@ page import="java.util.stream.Collectors" %>
<%@ page import="html.HtmlGenerator" %>
<%@ page import="java.util.function.Predicate" %>
<%@ page import="java.util.List" %>

<html>
    <head>
        <title>Search</title>
    </head>
    <body>
        <h1>Search results:</h1>
        <% File studentsXML = new File("D:\\students.xml");
        if (!studentsXML.exists()) {
            out.print("<h3>No students in the database</h3>");

            return;
        }

        String criteria = request.getParameter("criteria");
        String value = request.getParameter("value");

        if (criteria == null || criteria.isEmpty() ||
            value == null || value.isEmpty()) {
            response.sendError(400);

            return;
        }

        // TODO: Sometimes lambdas don't work. (Replace with anonymous classes)
        Predicate<StudentBean> filter =
            (criteria.equals("firstName")) ? studentBean -> studentBean.getFirstName().equals(value)
            : (criteria.equals("lastName")) ? studentBean -> studentBean.getLastName().equals(value)
            : (criteria.equals("age")) ? studentBean -> studentBean.getAge() == Integer.parseInt(value)
            : null;

        if (filter == null) {
            response.sendError(400);

            return;
        }

        XmlMapper mapper = new XmlMapper();
        StudentBean[] studentBeans = mapper.readValue(studentsXML, StudentBean[].class);

        Stream<StudentBean> stream = Arrays.stream(studentBeans);
        List<StudentBean> results = stream.filter(filter).collect(Collectors.toList());

        if (results.isEmpty()) {
            out.print("<p>Found no matches.</p>");
        } else {
            for (StudentBean result : results) {
                out.print(HtmlGenerator.toViewEntry(result));
            }
        } %>
    </body>
</html>