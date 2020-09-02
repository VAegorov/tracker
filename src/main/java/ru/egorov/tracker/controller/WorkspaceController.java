package ru.egorov.tracker.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.View;
import ru.egorov.tracker.domain.Project;
import ru.egorov.tracker.domain.User;
import ru.egorov.tracker.domain.issue.Issue;
import ru.egorov.tracker.domain.issue.IssuePriority;
import ru.egorov.tracker.domain.issue.IssueStatus;
import ru.egorov.tracker.repos.IssueRepo;
import ru.egorov.tracker.repos.ProjectRepo;
import ru.egorov.tracker.repos.UserRepo;

import javax.servlet.http.HttpServletRequest;
import java.util.HashSet;

@Controller
public class WorkspaceController {
    boolean isEditor;
    @Autowired
    private ProjectRepo projectRepo;
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private IssueRepo issueRepo;

    @PostMapping("/workspace")
    public String inWorkspace(@AuthenticationPrincipal User user, @RequestParam Long projectId, Model model) {
        Project project = projectRepo.findById(projectId).get();
        model.addAttribute("project", project);

        HashSet<User> executors = project.allUsers();
        model.addAttribute("executors", executors);

        model.addAttribute("user", user);

        Iterable<Issue> issues = issueRepo.findAllByProjectId(projectId);
        model.addAttribute("issues", issues);

        Iterable<User> users = userRepo.findNewUser(projectId);
        model.addAttribute("users", users);

        IssuePriority[] issuePriorities = IssuePriority.values();
        model.addAttribute("issuePriorities", issuePriorities);

        IssueStatus[] issueStatuses = IssueStatus.values();
        model.addAttribute("issueStatuses", issueStatuses);

        isEditor = false;
        if (project.isOwner(user) || project.isAdmin(user)) {
            isEditor = true;
        }
        model.addAttribute("isEditor", isEditor);

        return "/workspace";
    }

    @PostMapping("/addissue")
    public ModelAndView addIssue(@AuthenticationPrincipal User user, @RequestParam Long projectId,
                                 @RequestParam String issueName, @RequestParam String issueDescription,
                                 @RequestParam IssuePriority issuePriority, @RequestParam IssueStatus issueStatus,
                                 @RequestParam User executorId, HttpServletRequest request) {
        Project project = projectRepo.findById(projectId).get();

        if (!issueName.isEmpty() && !issueDescription.isEmpty()) {
            Issue issue = new Issue(issueName, issueDescription, user, executorId, project, issuePriority, issueStatus);
            project.getIssues().add(issue);
            projectRepo.save(project);
        }

        request.setAttribute(View.RESPONSE_STATUS_ATTRIBUTE, HttpStatus.TEMPORARY_REDIRECT);

        return new ModelAndView("redirect:/workspace");
    }

    @PostMapping("/addProjectUser")
    public ModelAndView addProjectUser(@AuthenticationPrincipal User user,
                                 @RequestParam Long userid,
                                 @RequestParam Long projectId, HttpServletRequest request) {
        User newUser = userRepo.findById(userid).get();
        Project project = projectRepo.findById(projectId).get();
        project.getUsers().add(newUser);
        projectRepo.save(project);
        newUser.getProjectsUser().add(project);
        userRepo.save(newUser);

        request.setAttribute(View.RESPONSE_STATUS_ATTRIBUTE, HttpStatus.TEMPORARY_REDIRECT);

        return new ModelAndView("redirect:/workspace");
    }
}
