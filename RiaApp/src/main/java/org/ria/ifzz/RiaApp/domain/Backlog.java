package org.ria.ifzz.RiaApp.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

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

    @LazyCollection(LazyCollectionOption.FALSE)
    @OneToMany(cascade = CascadeType.REFRESH, mappedBy = "backlog", orphanRemoval = true)
    private List<Result> results = new ArrayList<>();

    @LazyCollection(LazyCollectionOption.FALSE)
    @OneToMany(cascade = CascadeType.REFRESH, mappedBy = "backlog", orphanRemoval = true)
    private List<ControlCurve> controlCurves = new ArrayList<>();

    @LazyCollection(LazyCollectionOption.FALSE)
    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL, mappedBy = "backlog", orphanRemoval = true)
    @JsonIgnore
    private GraphCurve graphCurveList;

}
