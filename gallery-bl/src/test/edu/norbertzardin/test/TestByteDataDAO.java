package edu.norbertzardin.test;

import edu.norbertzardin.dao.ByteDataDao;
import edu.norbertzardin.dao.ImageDao;

import java.util.List;

import edu.norbertzardin.entities.ByteData;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

@ContextConfiguration(locations = "classpath:application-context-test.xml")
@RunWith(SpringJUnit4ClassRunner.class)
public class TestByteDataDAO {

    @Autowired
    private ByteDataDao byteDataDao;

    @Test
    @Transactional
    @Rollback(true)
    public void testCreateAndGetById() {
        byte[] d = {1,0,1,0};
        ByteData data = new ByteData();
        data.setData(d);
        byteDataDao.createByteData(data);
        Assert.assertEquals(d, byteDataDao.getByteDataById(data.getId()).getData());
    }
}
