package by.itacademy.library.web.command.impl;

import by.itacademy.library.entities.Book;
import by.itacademy.library.entities.BookAuthor;
import by.itacademy.library.service.AuthorService;
import by.itacademy.library.service.BookAuthorService;
import by.itacademy.library.service.BookService;
import by.itacademy.library.service.impl.AuthorServiceImpl;
import by.itacademy.library.service.impl.BookAuthorServiceImpl;
import by.itacademy.library.service.impl.BookServiceImpl;
import by.itacademy.library.web.command.Controller;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;

/**
 * Project KR. Created by masiuk-l on 20.08.2017.
 */
public class AddBookController implements Controller {
    private AuthorService authorService = AuthorServiceImpl.getInstance();
    private BookService bookService = BookServiceImpl.getInstance();
    private BookAuthorService bookAuthorService = BookAuthorServiceImpl.getInstance();

    @Override
    public void execute(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        req.getSession().setAttribute("authors", authorService.getAll());
        if (req.getMethod().equals("GET")) {
            RequestDispatcher dispatcher = req.getRequestDispatcher(MAIN_PAGE);
            dispatcher.forward(req, resp);
        } else {
            Book book = new Book();
            boolean validData = true; //flag to indicate whether all input data is valid
            if (req.getParameter("name").matches("^.{1,29}$")) {
                book.setName(req.getParameter("name"));
            } else {
                validData = false;
            }
            if (req.getParameter("isbn").matches("^[0-9\\\\-]{1,12}$")) {
                book.setIsbn(req.getParameter("isbn"));
            } else {
                validData = false;
            }
            if (req.getParameter("genre").matches("^.{1,30}$")) {
                book.setGenre(req.getParameter("genre"));
            } else {
                validData = false;
            }
            int year;
            try {
                year = Integer.parseInt(req.getParameter("year"));
                if (year <= LocalDate.now().getYear() && year > 0) {
                    book.setYear(year);
                } else {
                    validData = false;
                }
            } catch (NumberFormatException e) {
                validData = false;
            }
            int quantity;
            try {
                quantity = Integer.parseInt(req.getParameter("quantity"));
                if (quantity <= 999 && quantity > 0) {
                    book.setQuantity(quantity);
                } else {
                    validData = false;
                }
            } catch (NumberFormatException e) {
                validData = false;
            }

            if (validData) {
                bookService.save(book);
                String[] authorIDs = req.getParameterValues("author");
                for (String authorID : authorIDs) {
                    BookAuthor bookAuthor = new BookAuthor();
                    bookAuthor.setBookID(book.getBookID());
                    bookAuthor.setAuthorID(Integer.parseInt(authorID));
                    bookAuthorService.save(bookAuthor);
                }
                req.getSession().setAttribute("errorMsg", "");
                String contextPath = req.getContextPath();
                resp.sendRedirect(contextPath + "/frontController?command=catalog");
                return;
            } else { //forward user to the same page with error message
                req.getSession().setAttribute("errorMsg", "Invalid data. Please, retry");
                RequestDispatcher dispatcher = req.getRequestDispatcher(MAIN_PAGE);
                dispatcher.forward(req, resp);
                return;
            }
        }
    }
}
