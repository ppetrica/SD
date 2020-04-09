import ejb.CourseEntity;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


public class DeleteCourseServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String idString = request.getParameter("id");

        int id;
        try {
            id = Integer.parseInt(idString);
        } catch (Exception e) {
            response.sendError(504, "Invalid Course ID");
            return;
        }

        try {
            DBManager.removeEntity(id);
        } catch (Exception e) {
            response.sendError(504, "Failed to delete course from db");
            return;
        }

        response.sendRedirect("./show_courses.jsp");
    }
}
