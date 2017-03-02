package edu.norbertzardin.service;

import edu.norbertzardin.dao.ImageDao;
import edu.norbertzardin.dao.TagDao;
import edu.norbertzardin.entities.CatalogueEntity;
import edu.norbertzardin.entities.ImageEntity;
import edu.norbertzardin.entities.TagEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("imageService")
public class ImageService {

    @Autowired
    private ImageDao imageDao;

    @Autowired
    private TagDao tagDao;


    public void createImage(ImageEntity image){
        imageDao.createImage(image);
    }

    public ImageEntity getImageByIdWithFetch(Long id) { return imageDao.getImageByIdFullFetch(id); }

    public void deleteImage(ImageEntity ie) { imageDao.deleteImage(ie); }

    public void createTag(TagEntity tag) { tagDao.createTag(tag); }

    public void setImageDao(ImageDao imageDao){
        this.imageDao = imageDao;
    }

    public List<ImageEntity> getImageList() {
        return imageDao.getImageList();
    }

    public List<ImageEntity> getImageList(Integer page, Integer pageMax){
        return imageDao.getImageList(page, pageMax);
    }

    public Long getPageCount(Integer pageMax) {
        return imageDao.getPageCount(pageMax);
    }

    public ImageEntity getImageById(Long id) {
        return imageDao.getImageById(id);
    }

    public List<ImageEntity> findImagesByKeys(String key, TagEntity tag) {
        return imageDao.findImagesByKeys(key, tag);
    }

    public void editImage(ImageEntity ie) { imageDao.updateImage(ie); }

    public ImageEntity getImageByIdFullFetch(Long id) { return imageDao.getImageByIdFullFetch(id); }

    public List<ImageEntity> getImagesFromFolderForPage(Integer page,Integer pageMax, CatalogueEntity catalogue) {
        return imageDao.getImageListFromFolderForPage(page, pageMax, catalogue);
    }
}
