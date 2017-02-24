package edu.norbertzardin.dao;

import edu.norbertzardin.entities.ImageEntity;

import java.util.List;

public interface ImageDao {
    void createImage(ImageEntity image);

    void deleteImage(ImageEntity ie);

    List<ImageEntity> getImageList(Integer page, int pageMax);

    List<ImageEntity> getImageList();

    ImageEntity getImageById(int id);

    ImageEntity getImageByIdWithFetch(int id);

    List<ImageEntity> findImagesByName(String name);

    int getPageCount(int pageMax);
}
