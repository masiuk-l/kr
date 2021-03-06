package by.itacademy.library.dao.impl;

import by.itacademy.library.dao.LibrarianDAO;
import by.itacademy.library.entities.Librarian;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

/**
 * Project KR. Created by masiuk-l on 10.08.2017.
 */
public class LibrarianDAOImplTest {
    private LibrarianDAO librarianDAO;
    private Librarian librarian;

    @Before
    public void createLibrarian() {
        librarianDAO = LibrarianDAOImpl.getInstance();
        librarian = new Librarian();
        librarian.setName("Иван");
        librarian.setSecondName("Иванович");
        librarian.setSurname("Иванов");
        librarian.setEmail("ffr@ww");
        librarian.setPassword("fvfdcsdv");
    }

    @Test
    public void saveAndGetBySurname() throws Exception {
        librarian = librarianDAO.save(librarian);
        Librarian newLibrarian = librarianDAO.getBySurname("Иванов").get(0);
        librarian.setPassword(librarian.getPassword());
        Assert.assertEquals(librarian.toString(), newLibrarian.toString());
        librarianDAO.delete(newLibrarian.getLibrarianID());
    }


    @Test
    public void getAndUpdate() throws Exception {
        librarianDAO.save(librarian);
        String newSurname = "Иванова";
        librarian = librarianDAO.getBySurname("Иванов").get(0);
        librarian.setSurname(newSurname);
        librarianDAO.update(librarian);
        Librarian newLibrarian = librarianDAO.get(librarian.getLibrarianID());
        Assert.assertTrue(librarian.equals(newLibrarian));
        librarianDAO.delete(librarian.getLibrarianID());
    }

    @Test
    public void getAllAndDelete() throws Exception {

        librarianDAO.save(librarian);
        List<Librarian> librarians = librarianDAO.getAll();
        int oldSize = librarians.size();
        librarianDAO.delete(librarian.getLibrarianID());
        librarians = librarianDAO.getAll();
        Assert.assertEquals(oldSize - 1, librarians.size());
    }

}