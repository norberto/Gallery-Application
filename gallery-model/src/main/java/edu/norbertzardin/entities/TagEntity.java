package edu.norbertzardin.entities;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "NORBERT_TAGS", uniqueConstraints = {
        @UniqueConstraint(columnNames = "TAG_ID")
})
public class TagEntity implements Serializable {

    @Id
    @SequenceGenerator(name = "tagIdSeq", sequenceName = "NORBERT_TAG_ID_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "tagIdSeq")
    @Column(name = "TAG_ID")
    private int id;

    @Column(name ="NAME", length= 50)
    private String name;

    @Column(name = "CREATED_DATE", length = 100)
    private Date createdDate;

    @ManyToMany
    @JoinTable(
            name = "NORBERT_IMG_TAGS",
            joinColumns = @JoinColumn(name = "TAG_ID", referencedColumnName = "TAG_ID"),
            inverseJoinColumns = @JoinColumn(name = "IMAGE_ID", referencedColumnName = "IMAGE_ID"))
    private List<ImageEntity> images = new ArrayList<ImageEntity>();

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public int getId() {
        return id;
    }

    public void setId(int messageId) {
        this.id = messageId;
    }

    public TagEntity() {}

    public String toString(){
        return "id=" + id + " name=" + name + " created_at=" + createdDate ;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName(){return this.name; }

    public List<ImageEntity> getImages() {
        return images;
    }

    public void addImage(ImageEntity image) {
//        if(images == null) { images = new ArrayList<ImageEntity>(); }
        this.images.add(image); }

    public void setImages(List<ImageEntity> images) {
        this.images = images;
    }
}
