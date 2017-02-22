package edu.norbertzardin.entities;


import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "NORBERT_CATALOGUES", uniqueConstraints = {
        @UniqueConstraint(columnNames = "CATALOGUE_ID")
})
public class CatalogueEntity {
    @Id
    @SequenceGenerator(name = "messageIdSeq", sequenceName = "MESSAGE_ID_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "messageIdSeq")
    @Column(name = "CATALOGUE_ID")
    private int id;

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

    public int getId() {
        return id;
    }

    public void setId(int id) {
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
