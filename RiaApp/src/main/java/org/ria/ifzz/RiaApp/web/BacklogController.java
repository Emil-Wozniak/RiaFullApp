package org.ria.ifzz.RiaApp.web;

import org.ria.ifzz.RiaApp.domain.ProjectTask;
import org.ria.ifzz.RiaApp.services.MapValidationErrorService;
import org.ria.ifzz.RiaApp.services.ProjectTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;

@RestController
@RequestMapping("/api/backlog")
@CrossOrigin
public class BacklogController {

    @Autowired
    private ProjectTaskService projectTaskService;

    @Autowired
    MapValidationErrorService errorService;

    @PostMapping("/{backlog_id}")
    public ResponseEntity<?> addPTtoBacklog(@Valid @RequestBody ProjectTask projectTask, BindingResult result,
                                            @PathVariable String backlog_id, Principal principal) {

        ResponseEntity<?> errorMap = errorService.MapValidationService(result);
        if (errorMap != null) return errorMap;

        ProjectTask addProjectTask = projectTaskService.addProjectTask(backlog_id, projectTask, principal.getName());
        return new ResponseEntity<ProjectTask>(addProjectTask, HttpStatus.CREATED);

    }

    @GetMapping("/{backlog_id}")
    public Iterable<ProjectTask> getProjectBacklog(@PathVariable String backlog_id, Principal principal) {
        return projectTaskService.findBacklogById(backlog_id, principal.getName());
    }

    @GetMapping("/{backlog_id}/{pt_id}")
    public ResponseEntity<?> getProjectTask(@PathVariable String backlog_id, @PathVariable String pt_id, Principal principal) {
        ProjectTask projectTask = projectTaskService.findPTByProjectSequence(backlog_id, pt_id, principal.getName());
        return new ResponseEntity<ProjectTask>(projectTask, HttpStatus.OK);
    }

    @PatchMapping("/{backlog_id}/{pt_id}")
    public ResponseEntity<?> updatedProjectTask(@Valid @RequestBody ProjectTask projectTask, BindingResult result,
                                                @PathVariable String backlog_id, @PathVariable String pt_id,
                                                Principal principal) {

        ResponseEntity<?> errorMap = errorService.MapValidationService(result);
        if (errorMap != null) return errorMap;

        ProjectTask updatedProjectTask = projectTaskService.updatePTByProjectSequence(projectTask, backlog_id, pt_id, principal.getName());
        return new ResponseEntity<ProjectTask>(updatedProjectTask, HttpStatus.OK);

    }

    @DeleteMapping("/{backlog_id}/{pt_id}")
    public ResponseEntity<?> deleteProjectTask(@PathVariable String backlog_id, @PathVariable String pt_id, Principal principal) {
        projectTaskService.deletePTByProjectSequence(backlog_id, pt_id, principal.getName());
        return new ResponseEntity<String>("Project task '" + pt_id + "' was deleted successfully;", HttpStatus.OK);
    }
}
