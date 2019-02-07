package org.ria.ifzz.RiaApp.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
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

    private String fileId;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "file_entity_id", nullable = false)
    @JsonIgnore
    private FileEntity fileEntity;

    public FileData(String fileName, String contentType, Integer data) {
        this.fileName = fileName;
        this.contentType = contentType;
        this.data = data;
    }
}
