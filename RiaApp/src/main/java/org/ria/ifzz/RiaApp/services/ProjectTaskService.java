package org.ria.ifzz.RiaApp.services;

import org.ria.ifzz.RiaApp.domain.Backlog;
import org.ria.ifzz.RiaApp.domain.ProjectTask;
import org.ria.ifzz.RiaApp.exceptions.ProjectNotFoundException;
import org.ria.ifzz.RiaApp.repository.BacklogRepo;
import org.ria.ifzz.RiaApp.repository.ProjectRepo;
import org.ria.ifzz.RiaApp.repository.ProjectTaskRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProjectTaskService {

    @Autowired
    private BacklogRepo backlogRepo;

    @Autowired
    private ProjectTaskRepo projectTaskRepo;

    @Autowired
    private ProjectRepo projectRepo;

    @Autowired
    private ProjectService projectService;

    /**
     * @param projectIdentifier corresponding Project id
     * @param projectTask       object
     * @param username          principal user (Project owner)
     * @return object (projectTask) to be added to a specific project with corresponding id to it.
     * When project is not null and backlog exists set principal to user,
     * status from base null to 'TO_DO', and priority from base null to 3
     */
    public ProjectTask addProjectTask(String projectIdentifier, ProjectTask projectTask, String username) {

        Backlog backlog = projectService.findProjectByIdentifier(projectIdentifier, username).getBackLog();
        projectTask.setBacklog(backlog);
        Integer backlogSequence = backlog.getPTSequence();
        backlogSequence++;
        backlog.setPTSequence(backlogSequence);

        projectTask.setProjectSequence(projectIdentifier + "-" + backlogSequence);
        projectTask.setProjectIdentifier(projectIdentifier);
        if (projectTask.getStatus() == "" || projectTask.getStatus() == null) {
            projectTask.setStatus("TO_DO");
        }
        if (projectTask.getPriority() == null || projectTask.getPriority() == 0) {
            projectTask.setPriority(3);
        }
        return projectTaskRepo.save(projectTask);
    }


    public Iterable<ProjectTask> findBacklogById(String backlog_id, String username) {

        projectService.findProjectByIdentifier(backlog_id, username);
        return projectTaskRepo.findByProjectIdentifierOrderByPriority(backlog_id);
    }

    /**
     *
     * @param backlog_id      id of object to which project task is corresponding to
     * @param projectSequence project task id
     * @param username

     * @return object if backlog exists and if task exists, and if both backlog and task correspond to right project
     */
    public ProjectTask findPTByProjectSequence(String backlog_id, String projectSequence, String username) {
        projectService.findProjectByIdentifier(backlog_id, username);

        ProjectTask projectTask = projectTaskRepo.findByProjectSequence(projectSequence);
        if (projectTask == null) {
            throw new ProjectNotFoundException("Project task '" + projectSequence + "' not found");
        }
        if (!projectTask.getProjectIdentifier().equals(backlog_id)) {
            throw new ProjectNotFoundException("Project Task '" + projectSequence + "' does not exist in project: '" + backlog_id);
        }
        return projectTask;
    }

    public ProjectTask updatePTByProjectSequence(ProjectTask updatedTask, String backlog_id, String projectSequence, String username) {
        ProjectTask projectTask = findPTByProjectSequence(backlog_id, projectSequence, username);
        projectTask = updatedTask;
        return projectTaskRepo.save(projectTask);
    }

    /**
     * @param backlog_id      id of object to which project task is corresponding to
     * @param projectSequence project task id
     * @param username        principle of task owner
     * @return ProjectNotFoundException if project task doesn't exist or ids between backlog and project task don't match
     */
    public void deletePTByProjectSequence(String backlog_id, String projectSequence, String username) {
        ProjectTask projectTask = findPTByProjectSequence(backlog_id, projectSequence, username);
        projectTaskRepo.delete(projectTask);
    }
}
