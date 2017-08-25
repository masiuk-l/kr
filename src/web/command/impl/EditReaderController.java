package web.command.impl;

import entities.Reader;
import service.ReaderService;
import service.impl.ReaderServiceImpl;
import web.command.Controller;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;

public class EditReaderController implements Controller {
    private ReaderService newReaderService = ReaderServiceImpl.getInstance();

    @Override
    public void execute(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        if (req.getMethod().equals("GET")) {
            RequestDispatcher dispatcher = req.getRequestDispatcher(MAIN_PAGE);
            dispatcher.forward(req, resp);
            return;
        } else {
            Reader sessionReader = (Reader) req.getSession().getAttribute("sreader");
            Reader newReader = new Reader();
            boolean validData = true;
            if (req.getParameter("surname").matches("^[А-ЯЁ]([a-яё]){0,29}$") || req.getParameter("surname").length() == 0) {
                newReader.setSurname(req.getParameter("surname"));
            } else {
                validData = false;
            }
            if (req.getParameter("name").matches("^[А-ЯЁ]([a-яё]){0,29}$") || req.getParameter("name").length() == 0) {
                newReader.setName(req.getParameter("name"));
            } else {
                validData = false;
            }
            if (req.getParameter("secondname").matches("^[А-ЯЁ]([a-яё]){0,29}$") || req.getParameter("secondname").length() == 0) {
                newReader.setSecondName(req.getParameter("secondname"));
            } else {
                validData = false;
            }
            if (req.getParameter("em").matches("^([a-z0-9_\\.-]+\\@[\\da-z\\.-]+\\.[a-z\\.]{2,6})$") || req.getParameter("em").length() == 0) {
                newReader.setEmail(req.getParameter("em"));
            } else {
                validData = false;
            }
            if (req.getParameter("pass").matches(".{6,30}") || req.getParameter("pass").length() == 0) {
                newReader.setPassword(req.getParameter("pass"));
            } else {
                validData = false;
            }
            LocalDate birthday;
            try {
                birthday = LocalDate.parse(req.getParameter("birthday"));
                if (birthday.compareTo(LocalDate.now().minus(18, ChronoUnit.YEARS)) < 0) {
                    newReader.setBirthday(birthday);
                } else {
                    validData = false;
                }
            } catch (DateTimeParseException e) {
                validData = false;
            }
            if (req.getParameter("gender").equals("1"))
                newReader.setGender("male");
            else if (req.getParameter("gender").equals("2"))
                newReader.setGender("female");
            else validData = false;

            if (validData) {
                newReaderService.update(sessionReader, newReader);
                req.getSession().setAttribute("errorMsg", "");
                req.getSession().setAttribute("sreader", newReaderService.get(sessionReader.getReaderID()));
                String contextPath = req.getContextPath();
                resp.sendRedirect(contextPath + "/frontController?command=main");
                return;
            } else {
                req.getSession().setAttribute("errorMsg", "Invalid data. Please, retry");
                RequestDispatcher dispatcher = req.getRequestDispatcher(MAIN_PAGE);
                dispatcher.forward(req, resp);
                return;
            }

        }
    }
}
