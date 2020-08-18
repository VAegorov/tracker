package ru.egorov.tracker.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.egorov.tracker.domain.issue.Issue;
import ru.egorov.tracker.repos.IssuePepo;

@Controller
public class HomeController {
    @Autowired
    private IssuePepo issuePepo;

    @GetMapping("/")
    public String in() {

        return "in";
    }

    @GetMapping("/main")
    public String main(Model model) {
        Iterable<Issue> issues = issuePepo.findAll();
        model.addAttribute("issues", issues);

        return "main";
    }

    @PostMapping("/main")
    public String addIssue(@RequestParam String issueName, String issueDescription, Model model) {
        Issue issue = new Issue(issueName, issueDescription);
        issuePepo.save(issue);

        return "redirect:/main";
    }
}
