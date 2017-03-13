package edu.norbertzardin.service;

import edu.norbertzardin.dao.ImageDao;
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

    public void create(ImageEntity image){
        imageDao.save(image);
    }

    public ImageEntity loadMedium(Long id) {
        return imageDao.load(id, false, true, false, true);
    }

    public void remove(ImageEntity ie) { imageDao.remove(ie); }

    public List<ImageEntity> loadImages(Integer page, Integer pageMax){
        return imageDao.loadImages(page, pageMax);
    }

    public ImageEntity load(Long id) {
        return imageDao.load(id, false, false, false, false);
    }

    public List<ImageEntity> find(String key, TagEntity tag, Integer page, Integer pageMax) {
        return imageDao.find(key, tag, page, pageMax);
    }

    public void update(ImageEntity ie) { imageDao.update(ie); }

    public List<ImageEntity> loadCatalogueImages(Integer page,Integer pageMax, CatalogueEntity catalogue) {
        return imageDao.loadFromCatalogue(page, pageMax, catalogue);
    }

    public Long count(String searchString, TagEntity searchTag) {
        return imageDao.count(searchString, searchTag);
    }

    public ByteData download(Long id) {
        return imageDao.load(id, false, false, true, false).getDownload();
    }
}
