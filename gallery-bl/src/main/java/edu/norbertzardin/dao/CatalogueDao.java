package edu.norbertzardin.dao;

import edu.norbertzardin.entities.CatalogueEntity;

import java.util.List;

public interface CatalogueDao {
    void createCatalogue(CatalogueEntity ce);

    List<CatalogueEntity> getCatalogueList();

    void editCatalogue(CatalogueEntity ce);

    void deleteCatalogue(int id);

    CatalogueEntity getCatalogueByName(String name);

    CatalogueEntity getCatalogueById(int id);
}
