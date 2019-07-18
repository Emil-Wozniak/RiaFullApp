package org.ria.ifzz.RiaApp.models.results;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.MappedSuperclass;

@MappedSuperclass
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public abstract class ExaminationResult {

    private Integer probeNumber;
    private String position;
    private Integer cpm;
    private boolean flagged;
    private String ng;
}
