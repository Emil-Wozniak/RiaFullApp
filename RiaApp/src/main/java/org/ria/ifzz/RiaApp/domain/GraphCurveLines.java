package org.ria.ifzz.RiaApp.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class GraphCurveLines {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Double x = null;
    private Double y = null;
    private String dataId ="";
    private String fileName ="";

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="graphCurve_id", updatable = false, nullable = false)
    @JsonIgnore
    private GraphCurve graphCurve;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="backlog_id", updatable = false, nullable = false)
    @JsonIgnore
    private Backlog backlog;
}
