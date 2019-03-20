package org.ria.ifzz.RiaApp.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class GraphCurve {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Double x;
    private Double y;
    private Double r;
    private String fileName;
    private String dataId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="fileEntity_id", updatable = false, nullable = false)
    @JsonIgnore
    private FileEntity fileEntity;
}
