package service.impl;

import dao.FormDAO;
import dao.impl.FormDAOImpl;
import entities.Book;
import entities.Form;
import entities.Librarian;
import entities.Reader;
import service.FormService;
import service.ServiceException;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class FormServiceImpl extends AbstractService implements FormService {
    private FormDAO formDAO = FormDAOImpl.getInstance();

    @Override
    public Form save(Form form) {
        try {
            if (form != null) {
                startTransaction();
                form = formDAO.save(form);
                commit();
                return form;
            } else {
                throw new ServiceException("Form not defined");
            }
        } catch (SQLException e) {
            rollback();
            throw new ServiceException("Error creating Form");
        }

    }

    @Override
    public Form get(Serializable id) {
        try {
            Form form;
            startTransaction();
            form = formDAO.get(id);
            commit();
            return form;
        } catch (SQLException e) {
            rollback();
            throw new ServiceException("Error getting Form");
        }
    }

    @Override
    public void update(Form form) {
        try {
            startTransaction();
            formDAO.update(form);
            commit();
        } catch (SQLException e) {
            rollback();
            throw new ServiceException("Error updating Form");
        }
    }

    @Override
    public int delete(Serializable id) {
        try {
            startTransaction();
            int rows = formDAO.delete(id);
            commit();
            return rows;
        } catch (SQLException e) {
            rollback();
            throw new ServiceException("Error deleting Form");
        }
    }


    @Override
    public List<Form> getByReader(Reader reader) {
        ArrayList<Form> forms;
        try {
            startTransaction();
            forms = new ArrayList<>(formDAO.getByReader(reader));
            commit();
            return forms;
        } catch (SQLException e) {
            rollback();
            throw new ServiceException("Error finding Form");
        }
    }

    @Override
    public List<Form> getByLibrarian(Librarian librarian) {
        ArrayList<Form> forms;
        try {
            startTransaction();
            forms = new ArrayList<>(formDAO.getByLibrarian(librarian));
            commit();
            return forms;
        } catch (SQLException e) {
            rollback();
            throw new ServiceException("Error finding Form");
        }
    }

    @Override
    public List<Form> getByBook(Book book) {
        ArrayList<Form> forms;
        try {
            startTransaction();
            forms = new ArrayList<>(formDAO.getByBook(book));
            commit();
            return forms;
        } catch (SQLException e) {
            rollback();
            throw new ServiceException("Error finding Form");
        }
    }

    @Override
    public List<Form> getByReceivalType(String receivalType) {
        ArrayList<Form> forms;
        try {
            startTransaction();
            forms = new ArrayList<>(formDAO.getByReceivalType(receivalType));
            commit();
            return forms;
        } catch (SQLException e) {
            rollback();
            throw new ServiceException("Error finding Form");
        }
    }

    @Override
    public List<Form> getAll() {
        ArrayList<Form> forms;
        try {
            startTransaction();
            forms = new ArrayList<>(formDAO.getAll());
            commit();
            return forms;
        } catch (SQLException e) {
            rollback();
            throw new ServiceException("Error finding Form");
        }
    }
}