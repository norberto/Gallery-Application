package edu.norbertzardin.service;

import edu.norbertzardin.dao.CatalogueDao;
import edu.norbertzardin.entities.CatalogueEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.NoResultException;
import java.util.List;

@Service("catalogueService")
public class CatalogueService {

    @Autowired
    private CatalogueDao catalogueDao;

    public Boolean create(CatalogueEntity ce) {
        try {
            catalogueDao.loadNoFetch(ce.getTitle());
        } catch (NoResultException e) {
            catalogueDao.save(ce);
            return true;
        }

        return false;
    }

    public void remove(CatalogueEntity ce) {
        try {
            if (catalogueDao.load(ce.getId()) != null) {
                catalogueDao.remove(ce.getId());
//                return true; // successfully deleted
            }
        } catch (NoResultException e) {
            // Selected catalogue doesnt exist, therewore cannot be deleted. Do logging.
        }
//        return false;
    }

    public CatalogueEntity load(String name) {
        try {
            return catalogueDao.load(name);
        } catch (NoResultException e) {
            return null;
        }
    }

    public CatalogueEntity load(Long id) {
        return catalogueDao.load(id);
    }


    public List<CatalogueEntity> loadByPage(Integer cataloguePage, Integer pageCatalogueMax, String searchString, Boolean includeDefault) {
        return catalogueDao.loadByPage(cataloguePage, pageCatalogueMax, searchString, includeDefault);
    }

    public void setCatalogueDao(CatalogueDao catalogueDao) {
        this.catalogueDao = catalogueDao;
    }

    public void update(CatalogueEntity ce) {
        catalogueDao.update(ce);
    }

    public CatalogueEntity loadNoFetch(String name) {
        try {
            return catalogueDao.loadNoFetch(name);
        } catch (NoResultException e) {
            return null;
        }
    }

    public Integer imageCount(CatalogueEntity catalogue, Integer pageMax) {
        return countPages(catalogueDao.imageCount(catalogue), pageMax);
    }

    public List<CatalogueEntity> loadByKey(String key) {
        return catalogueDao.loadByKey(key);
    }

    public Integer count(Integer pageMax, String searchString, Boolean includeDefault) {
        Long result = catalogueDao.count(searchString);
        if (!includeDefault) {
            result--;
        }
        return countPages(result, pageMax);
    }

    private Integer countPages(Long count, Integer max) {
        Integer pageCount = count.intValue() / max;
        if (pageCount * max < count) {
            return pageCount + 1;
        } else {
            return pageCount;
        }
    }
}
