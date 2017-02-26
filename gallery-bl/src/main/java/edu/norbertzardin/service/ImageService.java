package edu.norbertzardin.service;

import edu.norbertzardin.dao.ByteDataDao;
import edu.norbertzardin.dao.ImageDao;
import edu.norbertzardin.dao.TagDao;
import edu.norbertzardin.entities.ByteData;
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

    public List<ImageEntity> getImageList(Integer page, int pageMax){
        return imageDao.getImageList(page, pageMax);
    }

    public Integer getPageCount(int pageMax) {
        return imageDao.getPageCount(pageMax);
    }

    public ImageEntity getImageById(Long id) {
        return imageDao.getImageById(id);
    }

    public List<ImageEntity> findImagesByName(String name) {
        return imageDao.findImagesByName(name);
    }

    public void editImage(ImageEntity ie) { imageDao.updateImage(ie); }

    public ImageEntity getImageByIdFullFetch(Long id) { return imageDao.getImageByIdFullFetch(id); }
}
