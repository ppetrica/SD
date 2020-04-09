import beans.StudentBean;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;


public class UpdateStudentServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String firstName = req.getParameter("firstName");
        String lastName = req.getParameter("lastName");
        String stringAge = req.getParameter("age");
        String stringId = req.getParameter("id");

        if (firstName == null || firstName.isEmpty() ||
                lastName == null || lastName.isEmpty() ||
                stringAge == null || stringAge.isEmpty() ||
                stringId == null || stringId.isEmpty()) {
            resp.sendError(400);

            return;
        }

        int age;
        try {
            age = Integer.parseInt(stringAge);
        } catch (NumberFormatException e) {
            resp.sendError(400);

            return;
        }

        int id;
        try {
            id = Integer.parseInt(stringId);
        } catch (NumberFormatException e) {
            resp.sendError(400);

            return;
        }


        File studentsXML = new File("D:\\students.xml");
        if (!studentsXML.exists()) {
            resp.sendError(400);

            return;
        }

        XmlMapper mapper = new XmlMapper();
        StudentBean[] currentStudentBeans = mapper.readValue(studentsXML, StudentBean[].class);

        for (StudentBean studentBean : currentStudentBeans) {
            if (studentBean.getId() == id) {
                studentBean.setFirstName(firstName);
                studentBean.setLastName(lastName);
                studentBean.setAge(age);

                break;
            }
        }

        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        mapper.writeValue(studentsXML, currentStudentBeans);

        req.getRequestDispatcher("./students.jsp").forward(req, resp);
    }
}
