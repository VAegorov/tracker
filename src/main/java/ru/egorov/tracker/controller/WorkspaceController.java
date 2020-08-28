package ru.egorov.tracker.controller;

import antlr.ASTNULLType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.egorov.tracker.domain.Project;
import ru.egorov.tracker.domain.User;
import ru.egorov.tracker.domain.issue.Issue;
import ru.egorov.tracker.repos.IssueRepo;
import ru.egorov.tracker.repos.ProjectRepo;
import ru.egorov.tracker.repos.UserRepo;

@Controller
public class WorkspaceController {
    @Autowired
    private ProjectRepo projectRepo;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private IssueRepo issueRepo;

    @PostMapping("/workspace")
    public String inWorkspace(@AuthenticationPrincipal User user, @RequestParam Long projectid, Model model) {
        Project project = projectRepo.findById(projectid).get();
        model.addAttribute("project", project);

        Iterable<Issue> issues = issueRepo.findAllByIdCreator(user.getId());
        model.addAttribute("issues", issues);

        return "/workspace";
    }

    @PostMapping("/addissue")
    public String addIssue(@AuthenticationPrincipal User user, @RequestParam Long projectid,
                           @RequestParam String issueName, @RequestParam String issueDescription, Model model) {
        Project project = projectRepo.findById(projectid).get();

        Issue issue = new Issue(issueName, issueDescription, user);
        project.getIssues().add(issue);
        projectRepo.save(project);

        Iterable<Issue> issues = issueRepo.findAllByIdCreator(user.getId());
        model.addAttribute("issues", issues);
        model.addAttribute("project", project);

        return "/workspace";
    }

    @PostMapping("/addProjectUser")
    public String addProjectUser(@AuthenticationPrincipal User user,
                                 @RequestParam Long userid,
                                 @RequestParam Long projectid,
                                 Model model) {
        User newUser = userRepo.findById(userid).get();
        Project project = projectRepo.findById(projectid).get();
        project.getUsers().add(newUser);
        projectRepo.save(project);
        newUser.getProjectsUser().add(project);
        userRepo.save(newUser);

        model.addAttribute("project", project);

        Iterable<User> users = userRepo.findAll();
        model.addAttribute("users", users);

        return "/workspace";
    }

    /*@GetMapping
    public String workspace() {
        return null;
    }*/
}
