package edu.norbertzardin.dao;

import edu.norbertzardin.entities.CatalogueEntity;

import java.util.List;

public interface CatalogueDao {
    void createCatalogue(CatalogueEntity ce);

    List<CatalogueEntity> getCatalogueListByPage(Integer cataloguePage, Integer pageCatalogueMax);

    List<CatalogueEntity> getCatalogueList();

    void editCatalogue(CatalogueEntity ce);

    void deleteCatalogue(Long id);

    CatalogueEntity getCatalogueByName(String name);

    CatalogueEntity getCatalogueByNameNoFetch(String name);

    CatalogueEntity getCatalogueById(Long id);

    CatalogueEntity getCatalogueByIdMediumFetch(Long id);

    Long getPageCount(CatalogueEntity catalogue);

    List<CatalogueEntity> getCatalogueListByKey(String key);

    Long getCatalogueCount();
}
