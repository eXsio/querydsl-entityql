package pl.exsio.querydsl.entityql.groovy.spring_data_jdbc.entity.it

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column

public class UploadedFile {

    @Id
    @Column("FILE_ID")
    private final UUID id;

    private final byte[] data;

    public UploadedFile(byte[] data, UUID id) {
        this.id = id;
        this.data = data;
    }

    public UUID getId() {
        return id;
    }

    byte[] getData() {
        return data
    }
}
