package org.ria.ifzz.RiaApp.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
@RequiredArgsConstructor
public class FileEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NonNull String fileName;
    @NonNull String contentType;
    @NonNull String dataId;
    @NonNull String fileOwner;

    @Setter
    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL, mappedBy = "fileEntity", orphanRemoval = true)
    @JsonIgnore
    private Backlog backlog;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    @NonNull User user;

    @Setter
    @JsonIgnore
    private byte[] data;
}
