package by.itacademy.library.service.impl;

import by.itacademy.library.entities.Librarian;
import by.itacademy.library.service.LibrarianService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

/**
 * Project KR. Created by masiuk-l on 12.08.2017.
 */
public class LibrarianServiceImplTest {
    private LibrarianService librarianService;
    private Librarian librarian;

    @Before
    public void createLibrarian() {
        librarianService = LibrarianServiceImpl.getInstance();
        librarian = new Librarian();
        librarian.setName("Иван");
        librarian.setSecondName("Иванович");
        librarian.setSurname("Иванов");
        librarian.setEmail("ffr@ww");
        librarian.setPassword("fvfdcsdv");
    }

    @Test
    public void saveAndGetBySurname() throws Exception {
        librarian = librarianService.save(librarian);
        Librarian newLibrarian = librarianService.getBySurname("Иванов").get(0);
        librarian.setPassword(librarian.getPassword());
        Assert.assertEquals(librarian.toString(), newLibrarian.toString());
        librarianService.delete(newLibrarian.getLibrarianID());
    }


    @Test
    public void getAndUpdate() throws Exception {
        librarianService.save(librarian);
        String newSurname = "Иванова";
        librarian = librarianService.getBySurname("Иванов").get(0);
        librarian.setSurname(newSurname);
        librarianService.update(librarian);
        Librarian newLibrarian = librarianService.get(librarian.getLibrarianID());
        Assert.assertTrue(librarian.equals(newLibrarian));
        librarianService.delete(librarian.getLibrarianID());
    }

    @Test
    public void getAllAndDelete() throws Exception {
        librarianService.save(librarian);
        List<Librarian> librarians = librarianService.getAll();
        int oldSize = librarians.size();
        librarianService.delete(librarian.getLibrarianID());
        librarians = librarianService.getAll();
        Assert.assertEquals(oldSize - 1, librarians.size());
    }


}