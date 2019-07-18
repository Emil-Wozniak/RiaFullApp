package org.ria.ifzz.RiaApp.models.graph;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import javax.persistence.*;

@Entity
@RequiredArgsConstructor
@Getter
@ToString
public class GraphLine {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NonNull String pattern;
    @NonNull Double x;
    @NonNull Double y ;
    @NonNull Double standard ;
    @NonNull Double meterReading;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="graph_id", updatable = false, nullable = false)
    @JsonIgnore
    @NonNull Graph graph;

}
