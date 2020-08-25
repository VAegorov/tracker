package ru.egorov.tracker.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.egorov.tracker.domain.Project;
//import ru.egorov.tracker.domain.ProjectUsers;
import ru.egorov.tracker.domain.User;
/*import ru.egorov.tracker.domain.issue.Issue;
import ru.egorov.tracker.repos.IssueRepo;*/
import ru.egorov.tracker.repos.ProjectRepo;
import ru.egorov.tracker.repos.UserRepo;
//import ru.egorov.tracker.repos.ProjectUsersRepo;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Controller
public class HomeController {
    /*@Autowired
    private IssueRepo issueRepo;*/

    @Autowired
    private ProjectRepo projectRepo;

    @Autowired
    private UserRepo userRepo;

   /* @Autowired
    private ProjectUsersRepo projectUsersRepo;*/

    @GetMapping("/")
    public String in() {

        return "in";
    }

    /*@GetMapping("/main")
    public String main(Model model) {
        Iterable<Issue> issues = issueRepo.findAll();
        model.addAttribute("issues", issues);

        return "main";
    }

    @PostMapping("/main")
    public String addIssue(@RequestParam String issueName, String issueDescription, Model model) {
        Issue issue = new Issue(issueName, issueDescription);
        issueRepo.save(issue);

        return "redirect:/main";
    }*/

@PostMapping("/home")
    public String addProject(@AuthenticationPrincipal User user, @RequestParam String projectName, Model model) {
        Project project = new Project(projectName, user);
        projectRepo.save(project);
        user.getProjects().add(project);
        userRepo.save(user);
        //userRepo.flush();
        //create table from user_project and include owner
        /*ProjectUsers projectUsers = new ProjectUsers(project);
        projectUsersRepo.save(projectUsers);*/



        return "redirect:/home";
    }

    @GetMapping("/home")
    public String home(@AuthenticationPrincipal User user, Model model) {
        //Iterable<Project> projects = projectRepo.findAll();
        //если user является создателем или участником project
       // if (user.equals())

        Iterable<Project> projects = projectRepo.findAllWhereById(user.getId());
        model.addAttribute("projects", projects);
        //Iterable<Project> projects = projectRepo.findAllWhereById(user.getId());
       /* if (!user.getProjects().isEmpty()) {
            Iterable<Project> projects = user.getProjects();
            System.out.println(777);
            model.addAttribute("projects", projects);
        }*/
        return "home";
    }
}
