package pl.exsio.querydsl.entityql.jpa.entity;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "JUPLOADED_FILES")
public class JUploadedFile {

    @Id
    @Column(name = "FILE_ID", nullable = false, columnDefinition = "UUID")
    @GeneratedValue
    private UUID id;

    @Lob
    @Column(name = "DATA", nullable =  false)
    private byte[] data;

    public JUploadedFile() {
    }

    public JUploadedFile(byte[] data, UUID id) {
        this.id = id;
        this.data = data;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    byte[] getData() {
        return data;
    }

    void setData(byte[] data) {
        this.data = data;
    }
}
