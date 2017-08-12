package dao.impl;

import dao.BookAuthorDAO;
import db.ConnectionManager;
import entities.Author;
import entities.Book;
import entities.BookAuthor;
import org.apache.log4j.Logger;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Project KR. Created by masiuk-l on 07.08.2017.
 */
public class BookAuthorDAOImpl implements BookAuthorDAO {

    private static final Logger LOG = Logger.getLogger(BookAuthorDAOImpl.class);
    private static final String saveBookAuthorQuery = "INSERT INTO BOOKS_AUTHORS (AUTHOR_ID, BOOK_ID) VALUES (?, ?)";
    private static final String updateBookAuthorQuery = "UPDATE BOOKS_AUTHORS SET AUTHOR_ID=?, BOOK_ID=? WHERE BOOK_AUTHOR_ID=?";
    private static final String getBookAuthorQuery = "SELECT * FROM BOOKS_AUTHORS WHERE BOOK_AUTHOR_ID=?";
    private static final String getAllBookAuthorsQuery = "SELECT * FROM BOOKS_AUTHORS";
    private static final String getBookAuthorByAuthorIDQuery = "SELECT * FROM BOOKS_AUTHORS WHERE AUTHOR_ID=?";
    private static final String getBookAuthorByBookIDQuery = "SELECT * FROM BOOKS_AUTHORS WHERE BOOK_ID=?";
    private static final String deleteBookAuthorQuery = "DELETE FROM BOOKS_AUTHORS WHERE BOOK_AUTHOR_ID=?";
    private static volatile BookAuthorDAO INSTANCE = null;
    private PreparedStatement psSave;
    private PreparedStatement psUpdate;
    private PreparedStatement psGet;
    private PreparedStatement psGetByAuthorID;
    private PreparedStatement psGetByBookID;
    private PreparedStatement psGetAll;
    private PreparedStatement psDelete;

    {
        try {
            psSave = ConnectionManager.getConnection().prepareStatement(saveBookAuthorQuery, Statement.RETURN_GENERATED_KEYS);
            psUpdate = ConnectionManager.getConnection().prepareStatement(updateBookAuthorQuery);
            psGet = ConnectionManager.getConnection().prepareStatement(getBookAuthorQuery);
            psGetByAuthorID = ConnectionManager.getConnection().prepareStatement(getBookAuthorByAuthorIDQuery);
            psGetByBookID = ConnectionManager.getConnection().prepareStatement(getBookAuthorByBookIDQuery);
            psGetAll = ConnectionManager.getConnection().prepareStatement(getAllBookAuthorsQuery);
            psDelete = ConnectionManager.getConnection().prepareStatement(deleteBookAuthorQuery);
        } catch (SQLException e) {
            LOG.error(e);
        }
    }

    private BookAuthorDAOImpl() {
    }

    public static BookAuthorDAO getInstance() {
        BookAuthorDAO bookAuthorDAO = INSTANCE;
        if (bookAuthorDAO == null) {
            synchronized (BookAuthorDAOImpl.class) {
                bookAuthorDAO = INSTANCE;
                if (bookAuthorDAO == null) {
                    INSTANCE = bookAuthorDAO = new BookAuthorDAOImpl();
                }
            }
        }

        return bookAuthorDAO;
    }

    private static void close(ResultSet rs) {
        try {
            if (rs != null)
                rs.close();
        } catch (SQLException e) {
            LOG.error(e);
        }
    }

    @Override
    public List<BookAuthor> getByAuthorID(Author author) throws SQLException {
        List<BookAuthor> list = new ArrayList<>();
        psGetByAuthorID.executeQuery();
        psGetByAuthorID.setInt(1, author.getAuthorID());
        psGetByAuthorID.execute();
        ResultSet rs = psGetByAuthorID.getResultSet();
        if (rs.next()) {
            BookAuthor bookAuthor = new BookAuthor();
            bookAuthor.setBookAuthorID(rs.getInt(1));
            bookAuthor.setAuthorID(rs.getInt(2));
            bookAuthor.setBookID(rs.getInt(3));
            list.add(bookAuthor);
        }
        close(rs);

        return list;
    }

    @Override
    public List<BookAuthor> getByBookID(Book book) throws SQLException {
        List<BookAuthor> list = new ArrayList<>();
        psGetByBookID.executeQuery();
        psGetByBookID.setInt(1, book.getBookID());
        psGetByBookID.execute();
        ResultSet rs = psGetByBookID.getResultSet();
        if (rs.next()) {
            BookAuthor bookAuthor = new BookAuthor();
            bookAuthor.setBookAuthorID(rs.getInt(1));
            bookAuthor.setAuthorID(rs.getInt(2));
            bookAuthor.setBookID(rs.getInt(3));
            list.add(bookAuthor);
        }
        close(rs);

        return list;
    }

    @Override
    public BookAuthor save(BookAuthor bookAuthor) throws SQLException {
        psSave.setInt(1, bookAuthor.getAuthorID());
        psSave.setInt(2, bookAuthor.getBookID());
        psSave.executeUpdate();
        ResultSet rs = psSave.getGeneratedKeys();
        if (rs.next()) {
            bookAuthor.setBookAuthorID(rs.getInt(1));
        }
        close(rs);
        return bookAuthor;
    }

    @Override
    public BookAuthor get(Serializable id) throws SQLException {
        psGet.setInt(1, (int) id);
        psGet.executeQuery();
        ResultSet rs = psGet.getResultSet();
        if (rs.next()) {
            BookAuthor bookAuthor = new BookAuthor();
            bookAuthor.setBookAuthorID(rs.getInt(1));
            bookAuthor.setAuthorID(rs.getInt(2));
            bookAuthor.setBookID(rs.getInt(3));
            return bookAuthor;
        }
        close(rs);

        return null;
    }

    @Override
    public void update(BookAuthor bookAuthor) throws SQLException {
        psUpdate.setInt(3, bookAuthor.getBookAuthorID());
        psUpdate.setInt(1, bookAuthor.getAuthorID());
        psUpdate.setInt(2, bookAuthor.getBookID());
        psUpdate.executeUpdate();
    }

    @Override
    public int delete(Serializable id) throws SQLException {
        psDelete.setInt(1, (int) id);
        return psDelete.executeUpdate();
    }

    @Override
    public List<BookAuthor> getAll() throws SQLException {
        List<BookAuthor> list = new ArrayList<>();
        psGetAll.executeQuery();
        ResultSet rs = psGetAll.getResultSet();
        if (rs.next()) {
            BookAuthor bookAuthor = new BookAuthor();
            bookAuthor.setBookAuthorID(rs.getInt(1));
            bookAuthor.setAuthorID(rs.getInt(2));
            bookAuthor.setBookID(rs.getInt(3));
            list.add(bookAuthor);
        }
        close(rs);

        return list;
    }
}