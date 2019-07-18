package org.ria.ifzz.RiaApp.models.results;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@RequiredArgsConstructor
@Getter
@ToString
public class ExaminationPoint extends ExaminationResult {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NonNull String identifier;
    @NonNull String pattern;
    @NonNull Integer probeNumber;
    @NonNull String position;
    @NonNull Integer cpm;
    @NonNull boolean flagged;
    @NonNull String ng;
}
