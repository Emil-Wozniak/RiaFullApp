package org.ria.ifzz.RiaApp.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@RequiredArgsConstructor
public class GraphCurve {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NonNull Double correlation;
    @NonNull String fileName;
    @NonNull String dataId;
    @NonNull Double zeroBindingPercent;
    @NonNull Double regressionParameterB;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "backlog_id", updatable = false, nullable = false)
    @JsonIgnore
    @NonNull Backlog backlog;

    @LazyCollection(LazyCollectionOption.FALSE)
    @OneToMany(cascade = CascadeType.REFRESH, mappedBy = "graphCurve", orphanRemoval = true)
    @NonNull List<GraphCurveLines> graphCurveLines = new ArrayList<>();
}
