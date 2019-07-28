package org.ria.ifzz.RiaApp.models.graph;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;

@Entity
@RequiredArgsConstructor
@NoArgsConstructor
@Getter
@ToString
public class GraphLine implements AbstractGraph{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NonNull String filename;
    @NonNull Double x;
    @NonNull Double y;
    @NonNull Double standard;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "graph_id", updatable = false, nullable = false)
    @JsonIgnore
    @NonNull Graph graph;

}
