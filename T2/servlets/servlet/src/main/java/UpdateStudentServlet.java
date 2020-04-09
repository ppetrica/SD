import ejb.StudentEntity;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


public class UpdateStudentServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response) throws IOException {
        String idString = request.getParameter("id");
        String nume = request.getParameter("nume");
        String prenume = request.getParameter("prenume");
        String varstaString = request.getParameter("varsta");

        int id;
        try {
            id = Integer.parseInt(idString);
        } catch (NumberFormatException e) {
            response.sendError(504, "Invalid ID specified");
            return;
        }

        int varsta;
        try {
            varsta = Integer.parseInt(varstaString);
        } catch (NumberFormatException e) {
            response.sendError(504, "Invalid age specified");
            return;
        }

        EntityManagerFactory factory =
                Persistence.createEntityManagerFactory("bazaDeDateSQLite");
        EntityManager em = factory.createEntityManager();


        StudentEntity student = em.find(StudentEntity.class, id);

        EntityTransaction transaction = em.getTransaction();
        transaction.begin();

        student.setNume(nume);
        student.setPrenume(prenume);
        student.setVarsta(varsta);

        transaction.commit();

        em.close();
        factory.close();

        response.sendRedirect("./fetch_student_list.jsp");
    }
}
