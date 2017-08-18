package VO.transfer;

import VO.FormVO;
import VO.ReaderVO;
import entities.Reader;

import java.util.List;

public class ReaderTransfer {
    public static ReaderVO toValueObject(Reader reader, List<FormVO> formVOS) {
        ReaderVO readerVO = new ReaderVO();
        readerVO.setReader(reader);
        readerVO.setFormVOS(formVOS);
        return readerVO;
    }

    public static Reader toEntity(ReaderVO readerVO) {
        Reader reader = readerVO.getReader();
        return reader;
    }
}
