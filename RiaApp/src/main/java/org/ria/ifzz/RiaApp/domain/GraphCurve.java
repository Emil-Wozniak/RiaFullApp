package org.ria.ifzz.RiaApp.domain;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@Data
public class GraphCurve {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    double x;
    double y;
}
