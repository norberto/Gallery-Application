package edu.norbertzardin.service;

import edu.norbertzardin.dao.CatalogueDao;
import edu.norbertzardin.entities.CatalogueEntity;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.jpa.JpaSystemException;
import org.springframework.stereotype.Service;

import javax.persistence.NoResultException;
import javax.persistence.PersistenceException;
import java.util.List;

@Service("catalogueService")
public class CatalogueService {

    final static Logger logger = Logger.getLogger(CatalogueService.class);
    private final CatalogueDao catalogueDao;

    @Autowired
    public CatalogueService(CatalogueDao catalogueDao) {
        this.catalogueDao = catalogueDao;
    }

    public Boolean create(CatalogueEntity ce) {
        try {
            catalogueDao.save(ce);
        } catch (JpaSystemException | PersistenceException e) {
            return false;
        }
        return true;

    }

    public Boolean remove(CatalogueEntity ce) {
        try {
            catalogueDao.remove(ce.getId());
            return true; // successfully deleted
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    public CatalogueEntity load(String name) {
        try {
            return catalogueDao.load(name);
        } catch (NoResultException e) {
            return null;
        }
    }

    public CatalogueEntity load(Long id) {
        try {
            return catalogueDao.load(id);
        } catch (NoResultException e) {
            return null;
        }
    }


    public List<CatalogueEntity> loadByPage(Integer cataloguePage, Integer pageCatalogueMax, String searchString, Boolean includeDefault) {
        return catalogueDao.loadByPage(cataloguePage, pageCatalogueMax, searchString, includeDefault);
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
