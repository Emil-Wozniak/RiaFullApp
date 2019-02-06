package org.ria.ifzz.RiaApp.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
public class FileData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String fileName;

    private String contentType;

    private Integer data;

    public FileData(String fileName, String contentType, Integer data) {
        this.fileName = fileName;
        this.contentType = contentType;
        this.data = data;
    }
}
