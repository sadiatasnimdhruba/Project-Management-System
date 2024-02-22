package com.springboot.finalproject;

import org.springframework.beans.BeanUtils;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.awt.*;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/projects")
public class ProjectController {

    private ProjectService projectService;
    private UserService userService;
    //private ReportService reportService;

    @ModelAttribute
    public void allPage(Model model){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();
        User loggedInUser = userService.getUserByEmail(currentPrincipalName);
        Long userId = loggedInUser.getId();
        String encodedImage = "data:image/png;base64,"+loggedInUser.getImage();
        model.addAttribute("encodedImage",encodedImage);
        model.addAttribute("user",loggedInUser);
    }

    public ProjectController(ProjectService projectService, UserService userService) {
        super();
        this.projectService = projectService;
        this.userService = userService;
        //this.reportService = reportService;
    }

    @GetMapping("/home")
    public String dashboard(Model model) {
        long totalProjectCount = projectService.getTotalProjectCount();
        model.addAttribute("totalProjects", totalProjectCount);

        long countDistinctOwners = projectService.getCountDistinctProjectOwners();
        model.addAttribute("countDistinctOwners", countDistinctOwners);

        long totalUserCount = userService.getTotalUserCount();
        model.addAttribute("totalUsers", totalUserCount);
        return "home";
    }

    @GetMapping("/owner")
    public String getAllOwnersWithProjects(Model model) {
        Map<User, List<Project>> ownersWithProjects = projectService.getAllOwnersWithProjects();

        model.addAttribute("ownersWithProjects", ownersWithProjects);

        return "owner";
    }

    @GetMapping // GET /projects
    public String listProjects(Model model) {

        model.addAttribute("projects", projectService.getAllProjects());
        List<Project> projectsWithOwnerUsername = projectService.getAllProjectsWithOwnerUsername();
        model.addAttribute("projects", projectsWithOwnerUsername);
        return "projects";
    }

    @GetMapping("/profile/{id}")
    public String profilePage(@PathVariable Long id, Model model) {
        return "profile";
    }


    @PostMapping(value="/profile/{id}" , consumes= MediaType.MULTIPART_FORM_DATA_VALUE)
    public String updateProfile(@PathVariable Long id,
                                @ModelAttribute("user") UserRegistrationDto registrationDto
                               , Model model){
        User existingUser = userService.getUserById(id);
        existingUser.setUsername(registrationDto.getUsername());
        existingUser.setFirstname(registrationDto.getFirstname());
        existingUser.setLastname(registrationDto.getLastname());
        existingUser.setAddress(registrationDto.getAddress());
        existingUser.setBirthdate(registrationDto.getBirthdate());
        existingUser.setImage(registrationDto.getImage());

        userService.updateUser(registrationDto);

        return "redirect:/projects?success1";
    }

    @GetMapping("/profile/change-password/{id}")
    public String changePassword(@PathVariable Long id, Model model) {

        return "change-password";
    }


    @GetMapping("/new") // GET /projects/new
    public String createProjectForm(Model model) {
        // create project object to hold project form data

        Project project = new Project();
        model.addAttribute("project", project);
        model.addAttribute("users", userService.getAllUsers());
        return "new-project";
    }


    @PostMapping("/projects") // POST /projects
    public String saveProject(Project project, Model model) {
        // save project to database
        projectService.saveProject(project);
        return "redirect:/projects";
    }

    @PostMapping
    public String saveProject(@ModelAttribute("project") Project project,
                              @RequestParam(name = "memberIds", required = false) ArrayList<Long> memberIds) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();
        System.out.println(currentPrincipalName);
        User loggedInUser = userService.getUserByEmail(currentPrincipalName);
        project.setOwner(loggedInUser);
        LocalDate currentDate = LocalDate.now();
        if (project.getStartDate() == null) {
            project.setStatus(0);
        } else if (project.getEndDate() != null && project.getEndDate().isBefore(currentDate)) {
            project.setStatus(3);
        } else if (project.getStartDate().isAfter(currentDate)) {
            project.setStatus(0);
        } else {
            project.setStatus(1);
        }
        // Set the selected members based on the provided memberIds
        List<User> members = new ArrayList<>();
        //System.out.println(memberIds);
        if (memberIds != null) {
            for (Long memberId : memberIds) {
                User member = userService.getUserById(memberId);
                member.getProjects().add(project);
                members.add(member);
            }
        }else {
            project.setMembers(null);
        }

        project.setMembers(members);
        projectService.saveProject(project);

        return "redirect:/projects?success";
    }

    //show projects by date
    @GetMapping("/date")
    public String listProjectsByDate(@RequestParam("startDate") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
                                     @RequestParam(name="endDate", required=false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate,
                                     Model model) {

        System.out.println(startDate);
        System.out.println(endDate);

        List<Project> projectsWithOwnerUsername = projectService.getAllProjectsWithOwnerUsername();
        model.addAttribute("projects", projectsWithOwnerUsername);

        model.addAttribute("projects", projectService.getAllProjectsBystartDateAndEndDate(startDate, endDate));
        List<Project> projects = projectService.getAllProjectsBystartDateAndEndDate(startDate, endDate);

        if(projects.isEmpty()){
            model.addAttribute("message", "No projects found");
            System.out.println("No projects found");
        }
        else{
            for (Project project : projects) {

                System.out.println(project.getName());
            }

        }

        return "projects";
    }

    @GetMapping("/details/{id}")
    public String projectDetails(@PathVariable Long id, Model model) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();
        System.out.println(currentPrincipalName);
        User loggedInUser = userService.getUserByEmail(currentPrincipalName);
        Long userId = loggedInUser.getId();
        model.addAttribute("user",loggedInUser);

        User owner = projectService.getProjectById(id).getOwner();

        //check if the logged in user is the owner of the project
        if (loggedInUser.getId() == owner.getId()) {
            model.addAttribute("isOwner", true);
        } else {
            model.addAttribute("isOwner", false);
        }

        //send the members list to the view
        List<User> members = projectService.getProjectById(id).getMembers();
        model.addAttribute("members", members);
        model.addAttribute("project", projectService.getProjectById(id));


        return "project-details";
    }

    @GetMapping("/{id}")
    public String deleteProject(@PathVariable Long id) {
        this.projectService.deleteProjectById(id);
        return "redirect:/projects";
    }


    @GetMapping("/edit/{id}")
    public String editProjectForm(@PathVariable Long id, Model model) {

        model.addAttribute("project", projectService.getProjectById(id));
        List<User> members = projectService.getProjectById(id).getMembers();
        model.addAttribute("members", members);
        List<User> users = userService.getAllUsers();
        model.addAttribute("users", users);
        return "update-project";
    }

    @PostMapping("/{id}")
    public String updateProject(@PathVariable Long id,
                                @ModelAttribute("project") Project project,
                                @RequestParam(name = "selectedmembers", required = false) List<Long> selectedmembers,
                                @RequestParam(name = "unselectedmembers", required = false) List<Long> unselectedmembers,
                                @RequestParam(name = "newOwner", required = false) Long newOwnerId) {

        Project existingProject = projectService.getProjectById(id);
        existingProject.setId(project.getId());
        existingProject.setName(project.getName());
        existingProject.setIntro(project.getIntro());
        existingProject.setStatus(project.calculateStatus());
        existingProject.setStartDate(project.getStartDate());
        existingProject.setEndDate(project.getEndDate());


       // List<User> existingMembers = existingProject.getMembers();

        // Remove members not selected in the form
        if (selectedmembers != null) {
            //existingProject.getMembers().removeIf(member -> !selectedmembers.contains(member.getId()));
            List<User> membersToRemove = new ArrayList<>();

            for (User existingMember : existingProject.getMembers()) {
                if (!selectedmembers.contains(existingMember.getId())) {
                    // If the existing member is not in the selectedMembers list, remove it
                    membersToRemove.add(existingMember);
                }
            }

            for (User memberToRemove : membersToRemove) {
                memberToRemove.getProjects().remove(existingProject);
                existingProject.getMembers().remove(memberToRemove);
            }
        }
        System.out.println(existingProject.getMembers());
        // Add new members to the project
        if (unselectedmembers != null) {
            for (Long memberId : unselectedmembers) {
                User member = userService.getUserById(memberId);
                if (!existingProject.getMembers().contains(member)) {
                    member.getProjects().add(existingProject);
                    existingProject.getMembers().add(member);
                }
            }
        }



        // Update the project with the modified members
        //existingProject.setMembers(existingMembers);

        User newOwner = userService.getUserById(newOwnerId);
        System.out.println(newOwner.getUsername());
        existingProject.setOwner(newOwner);


        projectService.updateProject(existingProject);

        return "redirect:/projects/details/" + id+"?success";
    }

//    @GetMapping("/changeOwner/{id}")
//    public String changeOwnerForm(@PathVariable Long id, Model model) {
//        model.addAttribute("project", projectService.getProjectById(id));
//        List<User> members = projectService.getProjectById(id).getMembers();
//        model.addAttribute("members", members);
//        List<User> users = userService.getAllUsers();
//        model.addAttribute("users", users);
//        return "change-owner";
//    }
//
//    @PostMapping("/changeOwner/{id}")
//    public String changeOwner(@PathVariable Long id,
//                              @RequestParam(name = "newOwner", required = false) Long newOwnerId) {
//        System.out.println("Coming here");
//        Project project = projectService.getProjectById(id);
//        User newOwner = userService.getUserById(newOwnerId);
//        System.out.println(newOwner.getUsername());
//        project.setOwner(newOwner);
//        projectService.saveProject(project);
//
//        return "redirect:/projects";
//
//    }










}
