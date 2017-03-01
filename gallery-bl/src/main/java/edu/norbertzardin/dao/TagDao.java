package edu.norbertzardin.dao;

import edu.norbertzardin.entities.TagEntity;

import java.util.List;

public interface TagDao {

    void createTag(TagEntity tag);

    List<TagEntity> getTagList();

    TagEntity getTagById(Long id);

    TagEntity getTagByIdWithFetch(Long id);

    TagEntity getTagByName(String name);

    void updateTag(TagEntity tag);

    void remove(TagEntity tag);

}