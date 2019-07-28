package org.ria.ifzz.RiaApp.models.graph;

import lombok.*;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@RequiredArgsConstructor
@ToString
public class Graph  implements AbstractGraph{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NonNull String identifier;
    @NonNull Double correlation;
    @NonNull Double zeroBindingPercent;
    @NonNull Double regressionParameterB;
    @NonNull Double regressionParameterA;

    @Setter
    @LazyCollection(LazyCollectionOption.FALSE)
    @OneToMany(cascade = CascadeType.REFRESH, mappedBy = "graph", orphanRemoval = true)
    @NonNull List<GraphLine> graphLines = new ArrayList<>();
}
