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

    public void deleteImage(int id) { imageDao.deleteImageById(id); }

    public void createTag(TagEntity tag) { tagDao.createTag(tag); }

    public void setImageDao(ImageDao imageDao){
        this.imageDao = imageDao;
    }

    public ImageDao getImageDao(){
        return imageDao;
    }

    public List<ImageEntity> getImageList(){
        return imageDao.getImageList();
    }

    public ImageEntity getImageById(int id) {
        return imageDao.getImageById(id);
    }

    public void pairImageWithTag(ImageEntity image, TagEntity tag){ tagDao.pair(image, tag); }


}
