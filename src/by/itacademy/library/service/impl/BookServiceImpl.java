package by.itacademy.library.service.impl;

import by.itacademy.library.VO.BookVO;
import by.itacademy.library.VO.transfer.BookTransfer;
import by.itacademy.library.dao.*;
import by.itacademy.library.dao.impl.*;
import by.itacademy.library.entities.*;
import by.itacademy.library.service.BookService;
import by.itacademy.library.service.ServiceException;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Project KR. Created by masiuk-l on 15.08.2017.
 *
 * Implementation of BookService
 */
public class BookServiceImpl extends AbstractService implements BookService {
    private static volatile BookService INSTANCE = null;

    private BookDAO bookDAO = BookDAOImpl.getInstance();
    private ReaderDAO readerDAO = ReaderDAOImpl.getInstance();
    private AuthorDAO authorDAO = AuthorDAOImpl.getInstance();
    private FormDAO formDAO = FormDAOImpl.getInstance();
    private BookAuthorDAO bookAuthorDAO = BookAuthorDAOImpl.getInstance();

    private BookServiceImpl() {
    }

    public static BookService getInstance() {
        BookService BookService = INSTANCE;
        if (BookService == null) {
            synchronized (BookServiceImpl.class) {
                BookService = INSTANCE;
                if (BookService == null) {
                    INSTANCE = BookService = new BookServiceImpl();
                }
            }
        }

        return BookService;
    }

    @Override
    public Book save(Book book) {
        try {
            if (book != null) {
                startTransaction();
                book = bookDAO.save(book);
                commit();
                return book;
            } else {
                throw new ServiceException("Book not defined");
            }
        } catch (SQLException e) {
            rollback();
            throw new ServiceException("Error creating Book", e);
        }

    }

    @Override
    public Book get(Serializable id) {
        try {
            Book book;
            startTransaction();
            book = bookDAO.get(id);
            commit();
            return book;
        } catch (SQLException e) {
            rollback();
            throw new ServiceException("Error getting Book", e);
        }
    }

    @Override
    public void update(Book book) {
        try {
            startTransaction();
            bookDAO.update(book);
            commit();
        } catch (SQLException e) {
            rollback();
            throw new ServiceException("Error updating Book", e);
        }
    }

    @Override
    public void update(Book oldBook, Book newBook) {
        Book book = new Book();
        book.setBookID(oldBook.getBookID());
        book.setName((newBook.getName().length() == 0) ? oldBook.getName() : newBook.getName());
        book.setIsbn((newBook.getIsbn().length() == 0) ? oldBook.getIsbn() : newBook.getIsbn());
        book.setGenre((newBook.getGenre().length() == 0) ? oldBook.getGenre() : newBook.getGenre());
        book.setYear((newBook.getYear() == 0) ? oldBook.getYear() : newBook.getYear());
        book.setQuantity(newBook.getQuantity());
        update(book);
    }

    @Override
    public int delete(Serializable id) {
        try {
            startTransaction();
            Book book = bookDAO.get(id);
            List<BookAuthor> bookAuthors = new ArrayList<>(bookAuthorDAO.getByBookID(book));
            for (BookAuthor bookAuthor : bookAuthors) {
                bookAuthorDAO.delete(bookAuthor.getBookAuthorID());
            }

            int rows = bookDAO.delete(id);
            commit();
            return rows;
        } catch (SQLException e) {
            rollback();
            throw new ServiceException("Error deleting Book", e);
        }
    }

    @Override
    public List<Book> getByName(String name) {
        ArrayList<Book> books;
        try {
            startTransaction();
            books = new ArrayList<>(bookDAO.getByName(name));
            commit();
            return books;
        } catch (SQLException e) {
            rollback();
            throw new ServiceException("Error finding Book", e);
        }
    }

    @Override
    public List<Book> searchByName(String name) {
        String searchKey = name.toLowerCase();
        ArrayList<Book> books = new ArrayList<>();
        try {
            startTransaction();
            ArrayList<Book> allBooks = new ArrayList<>(bookDAO.getAll());
            for (Book aBook : allBooks) {
                if (aBook.getName().toLowerCase().contains(searchKey) || aBook.getGenre().toLowerCase().contains(searchKey))
                    books.add(aBook);
            }
            commit();
            return books;
        } catch (SQLException e) {
            rollback();
            throw new ServiceException("Error finding Book", e);
        }
    }

    @Override
    public List<Book> getByIsbn(String isbn) {
        ArrayList<Book> books;
        try {
            startTransaction();
            books = new ArrayList<>(bookDAO.getByIsbn(isbn));
            commit();
            return books;
        } catch (SQLException e) {
            rollback();
            throw new ServiceException("Error finding Book", e);
        }
    }

    @Override
    public List<Book> getByGenre(String genre) {
        ArrayList<Book> books;
        try {
            startTransaction();
            books = new ArrayList<>(bookDAO.getByGenre(genre));
            commit();
            return books;
        } catch (SQLException e) {
            rollback();
            throw new ServiceException("Error finding Book", e);
        }
    }

    @Override
    public BookVO getBookVO(Book book) {
        try {
            startTransaction();
            List<BookAuthor> bookAuthors = new ArrayList<>(bookAuthorDAO.getByBookID(book));
            List<Author> authors = new ArrayList<>();
            for (BookAuthor bookAuthor : bookAuthors) {
                Author author = authorDAO.get(bookAuthor.getAuthorID());
                authors.add(author);
            }
            List<Form> forms = formDAO.getByBook(book);
            List<Reader> readers = new ArrayList<>();
            for (Form form : forms) {
                Reader reader = readerDAO.get(form.getReaderID());
                readers.add(reader);
            }
            BookVO bookVO = BookTransfer.toValueObject(book, readers, authors);
            commit();
            return bookVO;
        } catch (SQLException e) {
            rollback();
            throw new ServiceException("Error creating bookVO", e);
        }
    }

    @Override
    public List<Book> getAll() {
        ArrayList<Book> books;
        try {
            startTransaction();
            books = new ArrayList<>(bookDAO.getAll());
            commit();
            return books;
        } catch (SQLException e) {
            rollback();
            throw new ServiceException("Error finding Book", e);
        }
    }

}
