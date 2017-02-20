package edu.norbertzardin.entities;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "MESSAGE_TEST", uniqueConstraints = {
        @UniqueConstraint(columnNames = "ID")
})
public class MessageEntity implements Serializable {

    @Id
    @SequenceGenerator(name = "messageIdSeq", sequenceName = "MESSAGE_ID_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "messageIdSeq")
    @Column(name = "ID")
    private int id;

    @Column(name = "MESSAGE", length = 100)
    private String message;

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

    public MessageEntity(String message) {
        setMessage(message);
        setCreatedDate(new Date());
    }

    public MessageEntity() {}

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String toString(){
        return "id=" + id +", message=" + message;
    }
}