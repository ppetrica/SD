import beans.StudentBean;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;


public class AddStudentServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String firstName = req.getParameter("firstName");
        String lastName = req.getParameter("lastName");
        String stringAge = req.getParameter("age");

        if (firstName == null || firstName.isEmpty() ||
                lastName == null || lastName.isEmpty() ||
                stringAge == null || stringAge.isEmpty()) {
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

        XmlMapper mapper = new XmlMapper();

        File studentsXML = new File("D:\\students.xml");
        StudentBean[] studentBeans;
        int addedStudentIndex;
        if (studentsXML.exists()) {
            StudentBean[] currentStudents = mapper.readValue(studentsXML, StudentBean[].class);
            studentBeans = Arrays.copyOf(currentStudents, currentStudents.length + 1);

            addedStudentIndex = currentStudents.length;
        } else {
            studentBeans = new StudentBean[1];
            addedStudentIndex = 0;
        }

        StudentBean addedStudent = new StudentBean();
        addedStudent.setFirstName(firstName);
        addedStudent.setLastName(lastName);
        addedStudent.setAge(age);
        addedStudent.setId(addedStudentIndex);

        studentBeans[addedStudentIndex] = addedStudent;

        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        mapper.writeValue(studentsXML, studentBeans);

        req.getRequestDispatcher("./students.jsp").forward(req, resp);
    }
}
