package edu.norbertzardin.dao;

import edu.norbertzardin.entities.TagEntity;

import java.util.List;

public interface TagDao {

    void create(TagEntity tag);

    List<TagEntity> loadAll();

    TagEntity load(Long id);

    TagEntity load(String name, Boolean fetch);

    void update(TagEntity tag);

    void remove(TagEntity tag);

}
