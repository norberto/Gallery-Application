package edu.norbertzardin.dao;

import edu.norbertzardin.entities.CatalogueEntity;
import edu.norbertzardin.entities.ImageEntity;
import edu.norbertzardin.entities.TagEntity;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.PersistenceException;
import java.util.List;
@Transactional
public interface ImageDao {
    void save(ImageEntity image);

    void remove(Long id);

    void update(ImageEntity ie);

    Long count(String searchString, TagEntity searchTag);

    ImageEntity load(Long id, Boolean thumbnail, Boolean medium, Boolean download, Boolean tags);

    List<ImageEntity> loadAll(Integer page, Integer pageMax);

    List<ImageEntity> find(String keyword, TagEntity tag, Integer page, Integer pageMax);

    List<ImageEntity> loadFromCatalogue(Integer page, Integer pageMax, CatalogueEntity catalogue);
}
