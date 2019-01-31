package org.ria.ifzz.RiaApp.services;

import org.ria.ifzz.RiaApp.domain.Backlog;
import org.ria.ifzz.RiaApp.domain.Project;
import org.ria.ifzz.RiaApp.domain.User;
import org.ria.ifzz.RiaApp.exceptions.ProjectIdException;
import org.ria.ifzz.RiaApp.exceptions.ProjectNotFoundException;
import org.ria.ifzz.RiaApp.repository.BacklogRepo;
import org.ria.ifzz.RiaApp.repository.ProjectRepo;
import org.ria.ifzz.RiaApp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProjectService {

    @Autowired
    private ProjectRepo projectRepo;

    @Autowired
    private BacklogRepo backlogRepo;

    @Autowired
    private UserRepository userRepository;

    /**
     * @param project return object
     * @param username principal user
     * @return object with backlog set up within, if project leader is not correct it will not be upgraded
     */
    public Project saveOrUpdateProject(Project project, String username) {
        String getProjectID = project.getProjectIdentifier().toUpperCase();

        // project.getId != null
        // find by db id -> null
        if (project.getId() != null) {
            Project existingProject = projectRepo.findByProjectIdentifier(project.getProjectIdentifier());

            if (existingProject != null && (!existingProject.getProjectLeader().equals(username))) {
                throw new ProjectNotFoundException("Project not found in your account");
            } else if (existingProject == null){
                throw new ProjectNotFoundException("Project with ID: '"
                        + project.getProjectIdentifier() +
                        "' cannot be upgrade. It doesn't exist;");
            }
        }

        try {
            User user = userRepository.findByUsername(username);
            project.setUser(user);
            project.setProjectLeader(user.getUsername());

            project.setProjectIdentifier(getProjectID);

            if (project.getId() == null) {
                Backlog backlog = new Backlog();
                project.setBackLog(backlog);
                backlog.setProject(project);
                backlog.setProjectIdentifier(project.getProjectIdentifier().toUpperCase());
            }

            if (project.getId() != null) {
                project.setBackLog(backlogRepo.findByProjectIdentifier(getProjectID));
            }
            return projectRepo.save(project);
        } catch (Exception e) {
            throw new ProjectIdException("Project ID: " + project.getProjectIdentifier().toUpperCase() + " already exists");
        }
    }


    /**
     * @param projectId  the reference id which target object
     * @param username  the reference id which target principal for object
     * @return object with Id even if it's lowercase, check if exist, and if user is the owner of the object
     */
    public Project findProjectByIdentifier(String projectId, String username) {
        Project project = projectRepo.findByProjectIdentifier(projectId.toUpperCase());

        if (project == null) {
            throw new ProjectIdException(
                    "Project does not exist");
        }

        if (!project.getProjectLeader().equals(username)) {
            throw new ProjectNotFoundException("Project not found in your account");
        }

        return project;
    }

    public Iterable<Project> findAllProjects(String username) {
        return projectRepo.findAllByProjectLeader(username);
    }

    public void deleteProjectByIdentifier(String projectId, String username) {
        projectRepo.delete(findProjectByIdentifier(projectId, username));
    }
}
