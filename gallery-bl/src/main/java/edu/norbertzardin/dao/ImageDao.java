package edu.norbertzardin.dao;

import edu.norbertzardin.entities.ImageEntity;

import java.util.List;

public interface ImageDao {
    void createImage(ImageEntity image);

    void deleteImageById(int id);

    List<ImageEntity> getImageList();

    ImageEntity getImageById(int id);

    List<ImageEntity> findImagesByName(String name);
}
