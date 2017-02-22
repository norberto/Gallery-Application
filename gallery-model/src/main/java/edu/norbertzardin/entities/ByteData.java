package edu.norbertzardin.entities;

import javax.persistence.*;

@Entity
@Table(name = "NORBERT_BYTEDATA", uniqueConstraints = {
        @UniqueConstraint(columnNames = "ID")
})
public class ByteData {
    @Id
    @SequenceGenerator(name = "messageIdSeq", sequenceName = "MESSAGE_ID_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "messageIdSeq")
    @Column(name = "ID")
    private Long id;
    @Column(name = "DATA", length = 1000000)
    private byte[] data;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }
}
