package org.ria.ifzz.RiaApp.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
@NoArgsConstructor
public class FileEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String fileName;
    private String contentType;

    private String dataId;

    @JsonFormat(pattern = "yyyy-MM-dd' 'HH:mm:ss")
    @Column(updatable = false)
    private Date created_date;
    @JsonFormat(pattern = "yyyy-MM-dd' 'HH:mm:ss")
    private Date updated_date;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL, mappedBy = "fileEntity", orphanRemoval = true)
    @JsonIgnore
    private Backlog backlog;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    private User user;

    @Lob
    private byte[] data;

    public FileEntity(String fileName, String contentType, byte[] data) {
        this.fileName = fileName;
        this.contentType = contentType;
        this.data = data;
    }

    @PrePersist
    protected void onCreate() {
        this.created_date = new Date();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updated_date = new Date();
    }
}
