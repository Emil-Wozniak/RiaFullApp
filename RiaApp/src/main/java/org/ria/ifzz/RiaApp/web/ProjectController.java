package org.ria.ifzz.RiaApp.web;

import org.ria.ifzz.RiaApp.domain.Project;
import org.ria.ifzz.RiaApp.services.MapValidationErrorService;
import org.ria.ifzz.RiaApp.services.ProjectService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;

@RestController
@RequestMapping("/api/project")
@CrossOrigin(origins = "http://localhost:3000")
public class ProjectController {

    private final ProjectService projectService;
    private final MapValidationErrorService errorService;

    public ProjectController(ProjectService projectService, MapValidationErrorService errorService) {
        this.projectService = projectService;
        this.errorService = errorService;
    }

    /**
     *
     * @param project
     * @param result verify if project's body is correct
     * @param principal set project to login user
     * @return
     */
    @PostMapping("")
    public ResponseEntity<?> createNewProject(@Valid @RequestBody Project project,
                                              BindingResult result,
                                              Principal principal) {

        ResponseEntity<?> errorMap= errorService.MapValidationService(result);
        if (errorMap!=null) return errorMap;

        Project createdProject = projectService.saveOrUpdateProject(project, principal.getName());
        return new ResponseEntity<Project>(createdProject, HttpStatus.CREATED);
    }

    @GetMapping("/{projectId}")
    public ResponseEntity<?> getProjectId(@PathVariable String projectId, Principal principal){
        Project project = projectService.findProjectByIdentifier(projectId, principal.getName());
        return new ResponseEntity<Project>(project, HttpStatus.OK);
    }

    /**
     *
     * @param principal target a user
     * @return all project by username
     */
    @GetMapping("/all")
    public Iterable<Project> getAllProjects(Principal principal){return projectService.findAllProjects(principal.getName());}

    @DeleteMapping("/{projectId}")
    public ResponseEntity<?> deleteProject(@PathVariable String projectId, Principal principal){
        projectService.deleteProjectByIdentifier(projectId, principal.getName());
        return new ResponseEntity<String>("Project with ID: " + projectId + " was deleted", HttpStatus.OK);
    }
}
