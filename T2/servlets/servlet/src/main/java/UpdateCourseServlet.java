import ejb.CourseEntity;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


public class UpdateCourseServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response) throws IOException {
        String idString = request.getParameter("id");
        String nume = request.getParameter("nume");
        String universitate = request.getParameter("universitate");
        String nrStudentiString = request.getParameter("nrStudenti");

        int id;
        try {
            id = Integer.parseInt(idString);
        } catch (NumberFormatException e) {
            response.sendError(504, "Invalid ID specified");
            return;
        }

        int nrStudenti;
        try {
            nrStudenti = Integer.parseInt(nrStudentiString);
        } catch (NumberFormatException e) {
            response.sendError(504, "Invalid students number specified");
            return;
        }

        EntityManagerFactory factory =
                Persistence.createEntityManagerFactory("bazaDeDateSQLite");
        EntityManager em = factory.createEntityManager();

        CourseEntity Course = em.find(CourseEntity.class, id);

        EntityTransaction transaction = em.getTransaction();
        transaction.begin();

        Course.setNume(nume);
        Course.setUniversitate(universitate);
        Course.setNumarStudenti(nrStudenti);

        transaction.commit();

        em.close();
        factory.close();

        response.sendRedirect("./show_courses.jsp");
    }
}
