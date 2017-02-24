package edu.norbertzardin.service;

import edu.norbertzardin.dao.ByteDataDao;
import edu.norbertzardin.entities.ByteData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("dataService")
public class DataService {

    @Autowired
    private ByteDataDao byteDataDao;

    public void createByteData(ByteData bd) {
        byteDataDao.createByteData(bd);
    }

    public ByteData getByteDataById(Integer id) {
        return byteDataDao.getByteDataById(id);
    }


    public ByteDataDao getByteDataDao() {
        return byteDataDao;
    }

    public void setByteDataDao(ByteDataDao byteDataDao) {
        this.byteDataDao = byteDataDao;
    }
}