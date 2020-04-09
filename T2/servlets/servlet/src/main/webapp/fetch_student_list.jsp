<%@ page import="javax.persistence.EntityManagerFactory" %>
<%@ page import="javax.persistence.Persistence" %>
<%@ page import="javax.persistence.EntityManager" %>
<%@ page import="ejb.StudentEntity" %>
<%@ page import="javax.persistence.TypedQuery" %>
<%@ page import="java.util.List" %>
<!DOCTYPE html>
<html>
<head>
    <title>FetchStudentList</title>
</head>
<body>
    <h2>Lista studenti</h2>
    <table>
        <thead>
        <tr>
            <th>ID</th>
            <th>Nume</th>
            <th>Prenume</th>
            <th>Varsta</th>
            <th></th>
            <th></th>
        </tr>
        </thead>
        <tbody>
        <%
            EntityManagerFactory factory =
                Persistence.createEntityManagerFactory("bazaDeDateSQLite");

            EntityManager em = factory.createEntityManager();

            TypedQuery<StudentEntity> query = em.createQuery("SELECT student FROM StudentEntity student", StudentEntity.class);

            List<StudentEntity> results = query.getResultList();
        %>


        <% for (int i = 0; i < results.size(); ++i){ %>
        <% StudentEntity result = results.get(i); %>
            <tr>
                <td>
                    <form id="myform<%= i %>" action="./update-student" method="post">
                        <input type="text" disabled value="<%= result.getId() %>"/>
                        <input type="hidden" name="id" value="<%= result.getId() %>"/>
                    </form>
                </td>
                <td><input form="myform<%= i %>" type="text" name="nume" value="<%= result.getNume() %>"/></td>
                <td><input form="myform<%= i %>" type="text" name="prenume" value="<%= result.getPrenume() %>"/></td>
                <td><input form="myform<%= i %>" type="number" name="varsta" value="<%= result.getVarsta() %>"/></td>
                <td>
                    <form action="./delete-student" method="post">
                        <input type="hidden" name="id" value="<%= result.getId() %>"/>
                        <input type="submit" value="Delete"/>
                    </form>
                </td>
                <td>
                    <input form="myform<%= i %>" type="submit" value="Update"/>
                </td>
            </tr>
        </tbody>
        <% } %>
        <%
            em.close();
            factory.close();
        %>
    </table>
</body>
</html>
