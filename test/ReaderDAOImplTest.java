import dao.ReaderDAO;
import dao.impl.ReaderDAOImpl;
import entity.Reader;
import org.junit.Assert;
import org.junit.Test;

import java.sql.Date;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * Project KR. Created by masiuk-l on 06.08.2017.
 */
public class ReaderDAOImplTest {

    @Test
    public void saveAndGetBySurname() throws Exception {
        ReaderDAO readerDAO = ReaderDAOImpl.getInstance();
        Reader reader = new Reader();
        reader.setName("Иван");
        reader.setSecondName("Иванович");
        reader.setSurname("Иванов");
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.set(1976, 11, 27);
        reader.setBirthday(new Date(calendar.getTimeInMillis()));
        readerDAO.save(reader);
        Reader newReader = readerDAO.getReaderBySurname("Иванов");
        Assert.assertEquals(reader.getName(), newReader.getName());
        readerDAO.delete(newReader.getReaderID());
    }


    @Test
    public void getAndUpdate() throws Exception {
        ReaderDAO readerDAO = ReaderDAOImpl.getInstance();
        String oldSurname = "Хадькова";
        String newSurname = "Иванова";
        Reader reader = readerDAO.getReaderBySurname(oldSurname);
        reader.setSurname(newSurname);
        readerDAO.update(reader);
        Reader newReader = readerDAO.get(reader.getReaderID());
        Assert.assertEquals(newReader.getSurname(), newSurname);
        newReader.setSurname(oldSurname);
        readerDAO.update(newReader);
    }

    @Test
    public void getAllAndDelete() throws Exception {
        ReaderDAO readerDAO = ReaderDAOImpl.getInstance();
        Reader reader = new Reader();
        reader.setName("Иван");
        reader.setSecondName("Иванович");
        reader.setSurname("Иванов");
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.set(1976, 11, 27);
        reader.setBirthday(new Date(calendar.getTimeInMillis()));
        readerDAO.save(reader);
        List<Reader> readers = readerDAO.getAll();
        int oldSize = readers.size();
        readerDAO.delete(reader.getReaderID());
        readers = readerDAO.getAll();
        Assert.assertEquals(oldSize - 1, readers.size());
    }
}