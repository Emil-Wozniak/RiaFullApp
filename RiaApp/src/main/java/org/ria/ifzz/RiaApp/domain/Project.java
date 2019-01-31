package org.ria.ifzz.RiaApp.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Date;

@Data
@Entity
@NoArgsConstructor
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank(message = "is required")
    private String projectName;
    @NotBlank(message = "is required")
    @Size(min = 2, max = 6, message = "Please use 2-6 characters")
    @Column(updatable = false, unique = true)
    private String projectIdentifier;
    @NotBlank(message = "is required")
    private String description;
    @JsonFormat(pattern = "yyyy-mm-dd")
    private Date start_date;
    @JsonFormat(pattern = "yyyy-mm-dd")
    private Date end_date;
    @JsonFormat(pattern = "yyyy-mm-dd")
    @Column(updatable = false)
    private Date created_date;
    @JsonFormat(pattern = "yyyy-mm-dd")
    private Date updated_date;

    private String projectLeader;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL, mappedBy = "project")
    @JsonIgnore
    private Backlog backLog;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    private User user;

    @PrePersist
    protected void onCreate() {
        this.created_date = new Date();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updated_date = new Date();
    }

}
