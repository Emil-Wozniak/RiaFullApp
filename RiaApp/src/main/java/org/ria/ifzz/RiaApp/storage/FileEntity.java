package org.ria.ifzz.RiaApp.storage;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class FileEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String fileName;

    private String contentType;

    @Lob
    private byte[] data;

    public FileEntity() {
    }

    public FileEntity(String fileName, String contentType, byte[] data) {
        this.fileName = fileName;
        this.contentType = contentType;
        this.data = data;
    }
}
