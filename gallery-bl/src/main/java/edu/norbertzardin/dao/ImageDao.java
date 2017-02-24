package edu.norbertzardin.dao;

import edu.norbertzardin.entities.ImageEntity;
import org.zkoss.zul.Image;

import java.util.List;

public interface ImageDao {
    void createImage(ImageEntity image);

    void deleteImage(ImageEntity ie);

    List<ImageEntity> getImageList(Integer page, Integer pageMax);

    List<ImageEntity> getImageList();

    ImageEntity getImageById(Long id);

    ImageEntity getImageByIdWithFetch(Long id);

    ImageEntity getImageByIdFullFetch(Long id);

    List<ImageEntity> findImagesByName(String name);

    Integer getPageCount(Integer pageMax);

    void updateImage(ImageEntity ie);

}
