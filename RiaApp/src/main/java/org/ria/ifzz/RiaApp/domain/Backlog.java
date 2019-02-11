package org.ria.ifzz.RiaApp.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
public class Backlog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String fileName;
    private String contentType;

    private String dataId;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "file_entity_id", nullable = false)
    @JsonIgnore
    private FileEntity fileEntity;

    @OneToMany(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER, mappedBy = "backlog", orphanRemoval = true)
    private List<Result> results = new ArrayList<>();

}
