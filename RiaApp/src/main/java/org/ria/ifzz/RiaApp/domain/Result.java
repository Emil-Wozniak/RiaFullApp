package org.ria.ifzz.RiaApp.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
@NoArgsConstructor
public class Result {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String samples;
    private String position;
    private Double ccpm;
    private String fileName;
    private String dataId;
    private Double ngPerMl;

    @JsonFormat(pattern = "yyyy-MM-dd' 'HH:mm:ss")
    @Column(updatable = false)
    private Date created_at;
    @JsonFormat(pattern = "yyyy-MM-dd' 'HH:mm:ss")
    private Date update_at;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="backlog_id", updatable = false, nullable = false)
    @JsonIgnore
    private Backlog backlog;

    @PrePersist
    protected void onCreate(){
        this.created_at = new Date();
    }

    @PreUpdate
    protected void onUpdate(){
        this.update_at = new Date();
    }

}
