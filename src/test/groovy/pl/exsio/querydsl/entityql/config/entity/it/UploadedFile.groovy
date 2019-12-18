package pl.exsio.querydsl.entityql.config.entity.it

import javax.persistence.*

@Entity
@Table(name = "UPLOADED_FILES")
public class UploadedFile {

    @Id
    @Column(name = "FILE_ID", nullable = false, columnDefinition = "UUID")
    @GeneratedValue
    private UUID id;

    @Lob
    @Column(name = "DATA", nullable =  false)
    private byte[] data;

    public UploadedFile() {
    }

    public UploadedFile(byte[] data, UUID id) {
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
        return data
    }

    void setData(byte[] data) {
        this.data = data
    }
}
