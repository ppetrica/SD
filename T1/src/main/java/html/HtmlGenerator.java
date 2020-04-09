package html;

import beans.StudentBean;

public class HtmlGenerator {
    public static String toViewEntry(StudentBean studentBean) {
        return  "<form action=\"./update-student\" method=\"post\">" +
                "First Name: <input name=\"firstName\" value=\"" + studentBean.getFirstName() + "\"/><br/>" +
                "Last Name:  <input name=\"lastName\" value=\"" + studentBean.getLastName() + "\"/><br/>" +
                "Age:        <input name=\"age\" value=\"" + studentBean.getAge() + "\"/><br/>" +
                "<input name=\"id\" value=\"" + studentBean.getId() + "\" type=\"hidden\"/>" +
                "<input type=\"submit\" value=\"Update\">" +
                "</form>";
    }
}
