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

    public Boolean createCatalogue(CatalogueEntity ce) {
        CatalogueEntity catalogue = catalogueDao.getCatalogueByNameNoFetch(ce.getTitle());
        if (catalogue == null) {
            catalogueDao.createCatalogue(ce);
            return true;
        }
        return false;
    }

    public void deleteCatalogue(CatalogueEntity ce) {
        catalogueDao.deleteCatalogue(ce.getId());
    }

    public CatalogueEntity getCatalogueByName(String name) {
        try {
            return catalogueDao.getCatalogueByName(name);
        } catch (NoResultException e) {
            return null;
        }
    }

    public CatalogueEntity getCatalogueById(Long id) {
        return catalogueDao.getCatalogueById(id);
    }


    public List<CatalogueEntity> getCatalogueListByPage(Integer cataloguePage, Integer pageCatalogueMax) {
        return catalogueDao.getCatalogueListByPage(cataloguePage, pageCatalogueMax);
    }

    public List<CatalogueEntity> getCatalogueList() {
        return catalogueDao.getCatalogueList();
    }

    public CatalogueDao getCatalogueDao() {
        return catalogueDao;
    }

    public void setCatalogueDao(CatalogueDao catalogueDao) {
        this.catalogueDao = catalogueDao;
    }

    public void editCatalogue(CatalogueEntity ce) {
        catalogueDao.editCatalogue(ce);
    }

    public CatalogueEntity getCatalogueByIdMediumFetch(Long id) {
        return catalogueDao.getCatalogueByIdMediumFetch(id);
    }

    public CatalogueEntity getCatalogueByNameNoFetch(String name) {
        try {
            return catalogueDao.getCatalogueByNameNoFetch(name);
        } catch (NoResultException e) {
            return null;
        }
    }

    public Integer getPageCount(CatalogueEntity catalogue, Integer pageMax) {
        return countPages(catalogueDao.getPageCount(catalogue), pageMax);
    }

    public List<CatalogueEntity> getCatalogueListByKey(String key) {
        return catalogueDao.getCatalogueListByKey(key);
    }

    public Integer getCataloguePageCount(Integer pageMax) {
        return countPages(catalogueDao.getCatalogueCount() - 1, pageMax);
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
