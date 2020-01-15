package pl.exsio.querydsl.entityql.jdbc.entity.it

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column

import javax.annotation.Nonnull

public class UploadedFile {

    @Id
    @Column("FILE_ID")
    @Nonnull
    private final UUID id;
    @Nonnull
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
