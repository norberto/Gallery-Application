package edu.norbertzardin.service;

import edu.norbertzardin.dao.ByteDataDao;
import edu.norbertzardin.entities.ByteData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("dataService")
public class DataService {

    @Autowired
    private ByteDataDao byteDataDao;

    public void save(ByteData bd) {
        byteDataDao.save(bd);
    }

    public ByteData load(Long id) {
        return byteDataDao.load(id);
    }
}