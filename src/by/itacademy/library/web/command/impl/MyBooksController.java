package by.itacademy.library.web.command.impl;

import by.itacademy.library.VO.FormVO;
import by.itacademy.library.entities.Form;
import by.itacademy.library.entities.Reader;
import by.itacademy.library.service.BookService;
import by.itacademy.library.service.FormService;
import by.itacademy.library.service.impl.BookServiceImpl;
import by.itacademy.library.service.impl.FormServiceImpl;
import by.itacademy.library.web.command.Controller;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Project KR. Created by masiuk-l on 15.08.2017.
 */
public class MyBooksController implements Controller {
    private BookService bookService = BookServiceImpl.getInstance();
    private FormService formService = FormServiceImpl.getInstance();

    @Override
    public void execute(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (req.getSession().getAttribute("sreader") == null) {
            String contextPath = req.getContextPath();
            resp.sendRedirect(contextPath + "/frontController?command=error");
        }
        Reader reader = (Reader) req.getSession().getAttribute("sreader");
        ArrayList<Form> forms = new ArrayList<>(formService.getByReader(reader));
        ArrayList<FormVO> formVOS = new ArrayList<>();
        for (Form form : forms) {
            FormVO formVO = formService.getFormVO(form);
            formVOS.add(formVO);
        }
        req.getSession().setAttribute("formVOS", formVOS);
        req.getRequestDispatcher(MAIN_PAGE).forward(req, resp);
    }
}
