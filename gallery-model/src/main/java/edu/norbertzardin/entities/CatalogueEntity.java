package edu.norbertzardin.entities;


import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "NORBERT_CATALOGUES", uniqueConstraints = {
        @UniqueConstraint(columnNames = "CATALOGUE_ID")
})
public class CatalogueEntity {
    @Id
    @SequenceGenerator(name = "cataloguesIdSeq", sequenceName = "NORBERT_CATALOGUE_ID_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "cataloguesIdSeq")
    @Column(name = "CATALOGUE_ID")
    private Long id;

    @Column(name = "TITLE")
    private String title;

    @Column(name = "CREATED_DATE", length = 100)
    private Date createdDate;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "catalogue", cascade = CascadeType.ALL)
    private List<ImageEntity> images;

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<ImageEntity> getImages() {
        return images;
    }

    public void setImages(List<ImageEntity> images) {
        this.images = images;
    }
}
