package edu.norbertzardin.entities;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "NORBERT_TAGS", uniqueConstraints = {
        @UniqueConstraint(columnNames = "ID")
})
public class TagEntity implements Serializable {

    @Id
    @SequenceGenerator(name = "tagIdSeq", sequenceName = "NORBERT_TAG_ID_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "tagIdSeq")
    @Column(name = "ID")
    private int id;

    @Column(name ="NAME", length= 50)
    private String name;

    @Column(name = "CREATED_DATE", length = 100)
    private Date createdDate;

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


}
