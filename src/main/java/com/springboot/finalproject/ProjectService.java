package com.springboot.finalproject;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface ProjectService {
    List<Project> getAllProjects();
    List<Project> getAllProjectsBystartDateAndEndDate(LocalDate startDate, LocalDate endDate);
    List<Project> getAllProjectsWithOwnerUsername();
    Map<User, List<Project>> getAllOwnersWithProjects();

   Project getProjectById(Long id);
    long getTotalProjectCount();
    long getCountDistinctProjectOwners();
   Project saveProject(Project project);
   void deleteProjectById(Long id);
   Project updateProject(Project project);

}
