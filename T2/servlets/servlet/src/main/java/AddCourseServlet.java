import ejb.CourseEntity;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


public class AddCourseServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response) throws IOException {
        String nume = request.getParameter("nume");
        String universitate = request.getParameter("universitate");
        int nrStudenti = Integer.parseInt(request.getParameter("nrStudenti"));

        CourseEntity curs = new CourseEntity();
        curs.setNume(nume);
        curs.setUniversitate(universitate);
        curs.setNumarStudenti(nrStudenti);

        DBManager.saveEntity(curs);

        response.setContentType("text/html");
        response.getWriter().println("Datele au fost adaugate in baza de date." +
                "<br /><br /><a href='./'>Inapoi la meniul principal</a>");
    }
}
