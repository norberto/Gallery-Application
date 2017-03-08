package edu.norbertzardin.service;

import edu.norbertzardin.dao.CatalogueDao;
import edu.norbertzardin.entities.CatalogueEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("catalogueService")
public class CatalogueService {

    @Autowired
    private CatalogueDao catalogueDao;

    public void createCatalogue(CatalogueEntity ce) {
        catalogueDao.createCatalogue(ce);
    }

    public void deleteCatalogue(CatalogueEntity ce) {
        catalogueDao.deleteCatalogue(ce.getId());
    }

    public CatalogueEntity getCatalogueByName(String name) { return catalogueDao.getCatalogueByName(name); }

    public CatalogueEntity getCatalogueById(Long id) { return catalogueDao.getCatalogueById(id); }


    public List<CatalogueEntity> getCatalogueListByPage(Integer cataloguePage, Integer pageCatalogueMax) {
        return catalogueDao.getCatalogueListByPage(cataloguePage, pageCatalogueMax);
    }

    public List<CatalogueEntity> getCatalogueList() {
        return catalogueDao.getCatalogueList();
    }

    public void setCatalogueDao(CatalogueDao catalogueDao){
        this.catalogueDao = catalogueDao;
    }

    public CatalogueDao getCatalogueDao(){
        return catalogueDao;
    }

    public void editCatalogue(CatalogueEntity ce) {
        catalogueDao.editCatalogue(ce);
    }

    public CatalogueEntity getCatalogueByIdMediumFetch(Long id) { return catalogueDao.getCatalogueByIdMediumFetch(id); }

    public CatalogueEntity getCatalogueByNameNoFetch(String name) { return catalogueDao.getCatalogueByNameNoFetch(name); }

    public Long getPageCount(CatalogueEntity catalogue, Integer pageMax) { return catalogueDao.getPageCount(catalogue, pageMax); }

    public List<CatalogueEntity> getCatalogueListByKey(String key) { return  catalogueDao.getCatalogueListByKey(key); }

    public Long getCataloguePageCount(Integer pageMax) {
        return catalogueDao.getCataloguePageCount(pageMax);
    }
}
