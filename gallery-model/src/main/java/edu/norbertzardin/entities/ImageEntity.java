package edu.norbertzardin.entities;
import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "NORBERT_IMAGES", uniqueConstraints = {
        @UniqueConstraint(columnNames = "IMAGE_ID")
})
public class ImageEntity implements Serializable {

    @Id
    @SequenceGenerator(name = "messageIdSeq", sequenceName = "MESSAGE_ID_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "messageIdSeq")
    @Column(name = "IMAGE_ID")
    private int id;

    @Column(name ="NAME", length= 50)
    private String name;
    @Column(name = "DESCRIPTION", length = 500)
    private String description;

    @Column(name = "IMAGE_DATA", length = 1000000)
    private byte[] imageData;

//    @OneToOne
//    private ByteData imageData;

    @Column(name = "CREATED_DATE", length = 100)
    private Date createdDate;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "image", cascade = CascadeType.ALL)
    private List<TagEntity> tags;

    @ManyToOne
    @JoinColumn(name = "CATALOGUE_ID")
    private CatalogueEntity catalogue;

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

    public ImageEntity() {}

    public byte[] getImageData() {
        return imageData;
    }

    public void setImageData(byte[] data) {
        this.imageData = data;
    }
//
//    public ByteData getImageData() {
//        return imageData;
//    }
//
//    public void setImageData(ByteData data) {
//        this.imageData = data;
//    }

    public String toString(){
        return "id=" + id;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() { return this.description; }

    public void setName(String name) {
        this.name = name;
    }

    public String getName(){return this.name; }

    public CatalogueEntity getCatalogue() {
        return catalogue;
    }

    public void setCatalogue(CatalogueEntity catalogue) {
        this.catalogue = catalogue;
    }

    public List<TagEntity> getTags() { return tags; }

    public void setTags(List<TagEntity> tags) { this.tags = tags; }
}