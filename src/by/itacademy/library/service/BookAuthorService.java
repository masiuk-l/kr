package by.itacademy.library.service;

import by.itacademy.library.entities.Author;
import by.itacademy.library.entities.Book;
import by.itacademy.library.entities.BookAuthor;

import java.util.List;

/**
 * Project KR. Created by masiuk-l on 15.08.2017.
 */
public interface BookAuthorService extends Service<BookAuthor> {


    /**
     * @param author author
     * @return List of bookAuthors matching the input
     */
    List<BookAuthor> getByAuthorID(Author author);

    /**
     *
     * @param book book
     * @return List of bookAuthors matching the input
     */
    List<BookAuthor> getByBookID(Book book);


}
