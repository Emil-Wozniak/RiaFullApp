package org.ria.ifzz.RiaApp.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 * Represents
 */
@Entity
@Data
@NoArgsConstructor
public class ControlCurve  {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id = null;
    private Integer samples = null;
    private String position = "";
    private Double cpm = null;
    private String fileName = "";
    private String dataId = "";
    private boolean flagged = false;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="backlog_id", updatable = false, nullable = false)
    @JsonIgnore
    private Backlog backlog;
}
