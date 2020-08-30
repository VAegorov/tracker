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
import ru.egorov.tracker.repos.IssueRepo;
import ru.egorov.tracker.repos.ProjectRepo;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

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

        return "/issue";

    }

    @PostMapping("/editissue")
    ModelAndView issuePage(@RequestParam Long issueId, @RequestParam String issueName, HttpServletRequest request) {
        if (!issueName.isEmpty()) {
            Issue issue = issueRepo.findById(issueId).get();
            issue.setName(issueName);
            issueRepo.save(issue);
        }

        request.setAttribute(View.RESPONSE_STATUS_ATTRIBUTE, HttpStatus.TEMPORARY_REDIRECT);

        return new ModelAndView("redirect:/issue");
    }
}