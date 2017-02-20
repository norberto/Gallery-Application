package edu.norbertzardin.dao;

import edu.norbertzardin.entities.ImageEntity;
import edu.norbertzardin.entities.TagEntity;

import java.util.List;

public interface TagDao {
    void createTag(TagEntity tag);

    List<TagEntity> getTagList();

    TagEntity getTagById(int id);

    void pair(ImageEntity image, TagEntity tag);
}
