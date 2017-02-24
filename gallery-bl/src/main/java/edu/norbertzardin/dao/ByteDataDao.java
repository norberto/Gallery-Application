package edu.norbertzardin.dao;

import edu.norbertzardin.entities.ByteData;

public interface ByteDataDao {

    ByteData getByteDataById(Long id);

    void createByteData(ByteData bd);
}
