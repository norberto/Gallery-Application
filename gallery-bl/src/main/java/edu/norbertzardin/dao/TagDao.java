package edu.norbertzardin.dao;

import edu.norbertzardin.entities.TagEntity;

import javax.swing.text.html.HTML;
import java.util.List;

public interface TagDao {

    void createTag(TagEntity tag);

    List<TagEntity> getTagList();

    TagEntity getTagById(int id);

    TagEntity getTagByIdWithFetch(int id);

    TagEntity getTagByName(String name);

    void updateTag(TagEntity tag);
}
