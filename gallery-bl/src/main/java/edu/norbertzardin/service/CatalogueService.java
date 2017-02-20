package edu.norbertzardin.service;

import edu.norbertzardin.dao.CatalogueDao;
import edu.norbertzardin.dao.ImageDao;
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

}
