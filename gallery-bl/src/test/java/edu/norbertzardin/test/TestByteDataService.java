package edu.norbertzardin.test;

import edu.norbertzardin.entities.ByteData;
import edu.norbertzardin.service.DataService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@ContextConfiguration(locations = "classpath:application-context-test.xml")
@RunWith(SpringJUnit4ClassRunner.class)
public class TestByteDataService {

    @Autowired
    private DataService dataService;

    @Test
    @Rollback
    public void testDataSaveAndLoad() {
        byte[] d = {1, 0, 1, 0};
        ByteData data = new ByteData();
        data.setData(d);
        dataService.save(data);
        Assert.assertEquals(d, dataService.load(data.getId()).getData());
    }
}
