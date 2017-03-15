package edu.norbertzardin.dao;

import edu.norbertzardin.entities.CatalogueEntity;

import java.util.List;

public interface CatalogueDao {
    void save(CatalogueEntity ce);

    void update(CatalogueEntity ce);

    void remove(Long id);

    Long count(String searchString);

    CatalogueEntity load(String name);

    CatalogueEntity load(Long id);

    CatalogueEntity loadNoFetch(String name);

    Long imageCount(CatalogueEntity catalogue);

    List<CatalogueEntity> loadByPage(Integer cataloguePage, Integer pageCatalogueMax, String searchString, Boolean includeDefault);

}
