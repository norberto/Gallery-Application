package edu.norbertzardin.dao;

import edu.norbertzardin.entities.ByteData;

public interface ByteDataDao {

    void save(ByteData bd);

    ByteData load(Long id);
}
