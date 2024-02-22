package com.springboot.finalproject;

import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ProjectServiceImpl implements ProjectService {
    private ProjectRepository projectRepository;

    public ProjectServiceImpl(ProjectRepository projectRepository) {

        this.projectRepository = projectRepository;
    }

    @Override
    public List<Project> getAllProjects() {
        return projectRepository.findAll();
    }

    @Override
    public List<Project> getAllProjectsBystartDateAndEndDate(LocalDate startDate, LocalDate endDate) {
        List<Project> projectsByDate = new ArrayList<>();
        List<Project> projects = projectRepository.findAll();
        if (endDate == null) {
            for(Project project: projects)
            {
                if(project.getStartDate().isAfter(startDate) || project.getStartDate().isEqual(startDate) )
                    projectsByDate.add(project);

            } // Set a default value if 'endDate' is not provided
        }
        else {
            for (Project project : projects) {
                if ((project.getStartDate().isAfter(startDate) || project.getStartDate().isEqual(startDate)) && (project.getEndDate().isBefore(endDate) || project.getStartDate().isEqual(endDate)))
                    projectsByDate.add(project);

            }
        }

        return projectsByDate;
    }

    @Override
    public Project getProjectById(Long id) {

        return projectRepository.findById(id).get();
    }

    @Override
    public long getTotalProjectCount() {
        return projectRepository.count();
    }

   @Override
    public long getCountDistinctProjectOwners() {
    List<Project> allProjects = projectRepository.findAll();
    long distinctOwnersCount = allProjects.stream()
            .map(project -> project.getOwner()) // Assuming getProjectOwner() returns a User entity
            .filter(Objects::nonNull) // Exclude projects without an owner
            .distinct()
            .count();

    return distinctOwnersCount;
}
    @Override
    public Map<User, List<Project>> getAllOwnersWithProjects() {
        List<Project> allProjects = projectRepository.findAll();
        Map<User, List<Project>> ownersWithProjects = allProjects.stream()
                .collect(Collectors.groupingBy(Project::getOwner));

        return ownersWithProjects;
    }


    @Override
    public List<Project> getAllProjectsWithOwnerUsername() {
        List<Project> projects = projectRepository.findAll();

        return projects.stream()
                .peek(project -> project.setOwnerUsername(project.getOwner().getUsername()))
                .collect(Collectors.toList());
    }


    @Override
    public Project saveProject(Project project) {
        return projectRepository.save(project);
    }

    @Override
    public void deleteProjectById(Long id) {
        //first remove the project from the user's project list
        Project project = projectRepository.findById(id).get();
        project.getOwner().getProjects().remove(project);
        List<User> members = project.getMembers();
        for(User member: members)
        {
            member.getProjects().remove(project);
        }


        projectRepository.deleteById(id);
    }


    @Override
    public Project updateProject(Project project) {
        return projectRepository.save(project);
    }
}
