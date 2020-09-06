package ru.egorov.tracker.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.View;
import ru.egorov.tracker.domain.Project;
import ru.egorov.tracker.domain.User;
import ru.egorov.tracker.domain.issue.Issue;
import ru.egorov.tracker.domain.issue.IssuePriority;
import ru.egorov.tracker.domain.issue.IssueStatus;
import ru.egorov.tracker.domain.issue.SubIssue;
import ru.egorov.tracker.repos.IssueRepo;
import ru.egorov.tracker.repos.ProjectRepo;
import ru.egorov.tracker.repos.SubIssueRepo;
import ru.egorov.tracker.repos.UserRepo;

import javax.servlet.http.HttpServletRequest;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Controller
public class IssueController {
    @Autowired
    private ProjectRepo projectRepo;
    @Autowired
    private IssueRepo issueRepo;
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private SubIssueRepo subIssueRepo;


    @GetMapping("/issue")
    public String issuePage(@AuthenticationPrincipal User user, @RequestParam Long issueId, Model model) {
        Issue issue = issueRepo.findById(issueId).get();
        model.addAttribute("issue", issue);

        model.addAttribute("user", user);

        Project project = issue.getProject();
        model.addAttribute("project", project);

        //HashSet<User> executors = project.allUsers();
        model.addAttribute("executors", project.allUsers());

        //IssuePriority[] issuePriorities = IssuePriority.values();
        model.addAttribute("issuePriorities", IssuePriority.values());

        //IssueStatus[] issueStatuses = IssueStatus.values();
        model.addAttribute("issueStatuses", IssueStatus.values());

        if (!issue.getSubIssues().isEmpty()) {
            Set<SubIssue> subIssues = issue.getSubIssues();
            model.addAttribute("subissues", subIssues);
        }

        boolean resolveEditIssue;
        boolean resolveNoEditIssue;
        if (user.equals(issue.getCreator()) || user.equals(issue.getExecutor()) ||
                user.equals(project.getOwner()) || user.equals(project.getAdmin())) {
            resolveEditIssue = true;
            resolveNoEditIssue = false;
            model.addAttribute("resolveEditIssue", resolveEditIssue);
            model.addAttribute("resolveNoEditIssue", resolveNoEditIssue);
        } else {
            resolveEditIssue = false;
            resolveNoEditIssue = true;
            model.addAttribute("resolveEditIssue", resolveEditIssue);
            model.addAttribute("resolveNoEditIssue", resolveNoEditIssue);
        }

        return "/issue";

    }

    @PostMapping("/editissue")
    public String issuePage(@RequestParam Long issueId, @RequestParam String issueName,
                           @RequestParam String issueDescription, @RequestParam IssuePriority issuePriority,
                           @RequestParam IssueStatus issueStatus, @RequestParam Long executorId) {
        if (!issueName.isEmpty() || !issueDescription.isEmpty()) {
            Issue issue = issueRepo.findById(issueId).get();
            User executor = userRepo.findById(executorId).get();
            issue.setIssueStatus(issueStatus);
            issue.setExecutor(executor);
            issue.setName(issueName);
            issue.setDescription(issueDescription);
            issue.setIssuePriority(issuePriority);

            if (!issue.getSubIssues().isEmpty()) {
                for (SubIssue subissue : issue.getSubIssues()) {
                    subissue.setExecutor(executor);
                }
            }

            issueRepo.save(issue);
        }

        return "redirect:/issue?issueId=" + issueId;
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

    @PostMapping("/addsubissue")
    public String addsubissue(@AuthenticationPrincipal User user, @RequestParam Long issueId,
                              @RequestParam String issueName, @RequestParam String issueDescription,
                              @RequestParam IssuePriority issuePriority,
                              @RequestParam IssueStatus issueStatus) {

        //User executor = userRepo.findById(executorId).get();
        Issue issue = issueRepo.findById(issueId).get();
        User executor = issue.getExecutor();
        SubIssue subIssue = new SubIssue(issueName, issueDescription, user, executor,
                issuePriority, issueStatus, issue);
        //Project project = issue.getProject();
        //project
        issue.getSubIssues().add(subIssue);
        issueRepo.save(issue);

        return "redirect:/issue?issueId=" + issueId;
    }

    @GetMapping("/subissuepage")
    public String subissuePage(@AuthenticationPrincipal User user, @RequestParam Long subissueId, Model model) {
        SubIssue subIssue = subIssueRepo.findById(subissueId).get();
        Issue issue = subIssue.getIssue();
        Project project = issue.getProject();
        model.addAttribute(issue);
        model.addAttribute(subIssue);
        model.addAttribute(project);
        model.addAttribute("issuePriorities", IssuePriority.values());
        model.addAttribute("issueStatuses", IssueStatus.values());

        boolean resolveEditIssue;
        boolean resolveNoEditIssue;
        if (user.equals(issue.getCreator()) || user.equals(issue.getExecutor()) ||
                user.equals(project.getOwner()) || user.equals(project.getAdmin())) {
            resolveEditIssue = true;
            resolveNoEditIssue = false;
            model.addAttribute("resolveEditIssue", resolveEditIssue);
            model.addAttribute("resolveNoEditIssue", resolveNoEditIssue);
        } else {
            resolveEditIssue = false;
            resolveNoEditIssue = true;
            model.addAttribute("resolveEditIssue", resolveEditIssue);
            model.addAttribute("resolveNoEditIssue", resolveNoEditIssue);
        }

        return "/subissue";
    }

    @PostMapping("editsubissue")
    public String editsubissue(@AuthenticationPrincipal User user, @RequestParam Long issueId,
                               @RequestParam String issueName, @RequestParam String issueDescription,
                               @RequestParam IssuePriority issuePriority, @RequestParam IssueStatus issueStatus) {
        SubIssue subIssue = subIssueRepo.findById(issueId).get();
        subIssue.setName(issueName);
        subIssue.setDescription(issueDescription);
        subIssue.setIssuePriority(issuePriority);
        subIssue.setIssueStatus(issueStatus);
        subIssueRepo.save(subIssue);

        return "redirect:/subissuepage?subissueId=" + issueId;
    }

    @PostMapping("/deletesubissue")
    public String deletesubissue(@AuthenticationPrincipal User user,@RequestParam Long issueId) {
        SubIssue subIssue = subIssueRepo.findById(issueId).get();
        Issue issue = subIssue.getIssue();
        Long issueparentid = issue.getId();
        subIssue.getIssue().getSubIssues().remove(subIssue);
        issueRepo.save(issue);
        subIssueRepo.delete(subIssue);

        return "redirect:/issue?issueId=" + issueparentid;
    }
}