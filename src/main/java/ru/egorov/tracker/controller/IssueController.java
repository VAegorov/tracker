package ru.egorov.tracker.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
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
public class IssueController {
    @Autowired
    private ProjectRepo projectRepo;
    @Autowired
    private IssueRepo issueRepo;
    @Autowired
    private UserRepo userRepo;

    private boolean resolveEditIssue = false;
    private boolean resolveNoEditIssue = true;


    @PostMapping("/issue")
    public String issuePage(@AuthenticationPrincipal User user, @RequestParam Long issueId, Model model) {
        Issue issue = issueRepo.findById(issueId).get();
        model.addAttribute(issue);

        model.addAttribute("user", user);

        Project project = issue.getProject();
        model.addAttribute(project);

        HashSet<User> executors = project.allUsers();
        model.addAttribute("executors", executors);

        IssuePriority[] issuePriorities = IssuePriority.values();
        model.addAttribute("issuePriorities", issuePriorities);

        IssueStatus[] issueStatuses = IssueStatus.values();
        model.addAttribute("issueStatuses", issueStatuses);

        if (user.equals(issue.getCreator()) || user.equals(issue.getExecutor()) || user.equals(project.getOwner()) || user.equals(project.getAdmin())) {
            resolveEditIssue = true;
            model.addAttribute("resolveEditIssue", resolveEditIssue);
            resolveNoEditIssue = false;
            model.addAttribute("resolveNoEditIssue", resolveNoEditIssue);
        } else {
            model.addAttribute("resolveEditIssue", resolveEditIssue);
            model.addAttribute("resolveNoEditIssue", resolveNoEditIssue);
        }

        return "/issue";

    }

    @PostMapping("/editissue")
    public ModelAndView issuePage(@RequestParam Long issueId, @RequestParam String issueName,
                           @RequestParam String issueDescription, @RequestParam IssuePriority issuePriority,
                           @RequestParam IssueStatus issueStatus, @RequestParam Long executorId, HttpServletRequest request) {
        if (!issueName.isEmpty() || !issueDescription.isEmpty()) {
            Issue issue = issueRepo.findById(issueId).get();
            User executor = userRepo.findById(executorId).get();
            issue.setIssueStatus(issueStatus);
            issue.setExecutor(executor);
            issue.setName(issueName);
            issue.setDescription(issueDescription);
            issue.setIssuePriority(issuePriority);

            issueRepo.save(issue);
        }

        request.setAttribute(View.RESPONSE_STATUS_ATTRIBUTE, HttpStatus.TEMPORARY_REDIRECT);

        return new ModelAndView("redirect:/issue");
    }

    @PostMapping("/deleteissue")
    public ModelAndView deleteissue(@AuthenticationPrincipal User user, @RequestParam Long issueId,
                                    @RequestParam Long projectId, HttpServletRequest request) {

        Project project = projectRepo.findById(projectId).get();
        Issue issue = issueRepo.findById(issueId).get();
        issue.getExecutor().getIssueExecutor().remove(issue);
        issue.getCreator().getIssueCreator().remove(issue);
        project.getIssues().remove(issue);
        issueRepo.deleteById(issueId);
        projectRepo.save(project);

        request.setAttribute(View.RESPONSE_STATUS_ATTRIBUTE, HttpStatus.TEMPORARY_REDIRECT);

        return new ModelAndView("redirect:/workspace");
    }
}