package org.ria.ifzz.RiaApp.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

//@Entity
@NoArgsConstructor
@Data
public class Result {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    public List<String> samples;
    public List<String> position;
    public List<String> ccpm;
    @JsonFormat(pattern = "yyyy-mm-dd")
    private Date dueDate;
    @JsonFormat(pattern = "yyyy-mm-dd")
    @Column(updatable = false)
    private Date created_at;
    @JsonFormat(pattern = "yyyy-mm-dd")
    private Date update_at;

    @PrePersist
    protected void onCreate(){
        this.created_at = new Date();
    }

    @PreUpdate
    protected void onUpdate(){
        this.update_at = new Date();
    }

}
