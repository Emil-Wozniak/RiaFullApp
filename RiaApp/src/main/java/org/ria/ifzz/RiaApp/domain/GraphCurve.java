package org.ria.ifzz.RiaApp.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
public class GraphCurve {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Double correlation = null;
    private String fileName;
    private String dataId;
    private Double zeroBindingPercent = null;
    private Double regressionParameterB;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="backlog_id", updatable = false, nullable = false)
    @JsonIgnore
    private Backlog backlog;

    @LazyCollection(LazyCollectionOption.FALSE)
    @OneToMany(cascade = CascadeType.REFRESH, mappedBy = "graphCurve", orphanRemoval = true)
    private List<GraphCurveLines> graphCurveLines = new ArrayList<>();
}
