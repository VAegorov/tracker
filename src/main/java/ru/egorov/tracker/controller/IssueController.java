package ru.egorov.tracker.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.View;
import ru.egorov.tracker.domain.Project;
import ru.egorov.tracker.domain.issue.Issue;
import ru.egorov.tracker.domain.issue.IssuePriority;
import ru.egorov.tracker.domain.issue.IssueStatus;
import ru.egorov.tracker.repos.IssueRepo;
import ru.egorov.tracker.repos.ProjectRepo;

import javax.servlet.http.HttpServletRequest;

@Controller
public class IssueController {
    @Autowired
    private ProjectRepo projectRepo;
    @Autowired
    private IssueRepo issueRepo;

    @PostMapping("/issue")
    String issuePage(@RequestParam Long issueId, Model model) {
        Issue issue = issueRepo.findById(issueId).get();
        model.addAttribute(issue);

        Project project = issue.getProject();
        model.addAttribute(project);

        IssuePriority[] issuePriorities = IssuePriority.values();
        model.addAttribute("issuePriorities", issuePriorities);

        IssueStatus[] issueStatuses = IssueStatus.values();
        model.addAttribute("issueStatuses", issueStatuses);

        return "/issue";

    }

    @PostMapping("/editissue")
    ModelAndView issuePage(@RequestParam Long issueId, @RequestParam String issueName,
                           @RequestParam String issueDescription, @RequestParam IssuePriority issuePriority,
                           @RequestParam IssueStatus issueStatus, HttpServletRequest request) {
        if (!issueName.isEmpty() || !issueDescription.isEmpty()) {
            Issue issue = issueRepo.findById(issueId).get();
            issue.setIssueStatus(issueStatus);
            issue.setName(issueName);
            issue.setDescription(issueDescription);
            issue.setIssuePriority(issuePriority);

            issueRepo.save(issue);
        }

        request.setAttribute(View.RESPONSE_STATUS_ATTRIBUTE, HttpStatus.TEMPORARY_REDIRECT);

        return new ModelAndView("redirect:/issue");
    }
}