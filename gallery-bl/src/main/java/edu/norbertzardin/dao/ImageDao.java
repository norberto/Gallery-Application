package edu.norbertzardin.dao;

import edu.norbertzardin.entities.CatalogueEntity;
import edu.norbertzardin.entities.ImageEntity;
import edu.norbertzardin.entities.TagEntity;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
@Transactional
public interface ImageDao {
    void createImage(ImageEntity image);

    void deleteImage(ImageEntity ie);

    List<ImageEntity> getImageList(Integer page, Integer pageMax);

    List<ImageEntity> getImageList();

    ImageEntity getImageById(Long id);

    ImageEntity getImageByIdWithFetch(Long id);

    ImageEntity getImageByIdFullFetch(Long id);

    List<ImageEntity> findImagesByKeys(String keyword, TagEntity tag);

    Long getPageCount(Integer pageMax);

    void updateImage(ImageEntity ie);

    List<ImageEntity> getImageListFromFolderForPage(Integer page, Integer pageMax, CatalogueEntity catalogue);
}
