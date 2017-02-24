package edu.norbertzardin.dao;

import edu.norbertzardin.entities.ByteData;

public interface ByteDataDao {

    ByteData getByteDataById(Integer id);

    void createByteData(ByteData bd);
}
