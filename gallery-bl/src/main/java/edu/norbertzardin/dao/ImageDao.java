package edu.norbertzardin.dao;

import edu.norbertzardin.entities.ImageEntity;

import java.util.List;

public interface ImageDao {
    void createImage(ImageEntity image);

    void deleteImage(ImageEntity ie);

    List<ImageEntity> getImageList(Integer page, Integer pageMax);

    List<ImageEntity> getImageList();

    ImageEntity getImageById(Long id);

    ImageEntity getImageByIdWithFetch(Long id);

    ImageEntity getImageByIdFullFetch(Long id);

    List<ImageEntity> findImagesByKey(String keyword);

    Integer getPageCount(Integer pageMax);

    void updateImage(ImageEntity ie);

}
