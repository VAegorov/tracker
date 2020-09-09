package ru.egorov.tracker.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
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
import java.util.Optional;

@Controller
public class WorkspaceController {
    boolean isEditor;
    @Autowired
    private ProjectRepo projectRepo;
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private IssueRepo issueRepo;

    @GetMapping("/workspace")
    public String inWorkspace(@AuthenticationPrincipal User user, @RequestParam Long projectId, Model model) {
        Project project = projectRepo.findById(projectId).get();
        model.addAttribute("project", project);

        HashSet<User> executors = project.allUsers();
        model.addAttribute("executors", executors);

        model.addAttribute("user", user);

        Iterable<Issue> issuesBackLog = issueRepo.findAllByProjectIdAndIsBackLogIsTrue(projectId);
        model.addAttribute("issuesbacklog", issuesBackLog);

        Iterable<Issue> issuesSprint = issueRepo.findAllByProjectIdAndIsBackLogIsFalse(projectId);
        model.addAttribute("issuessprint", issuesSprint);



        Iterable<User> users = userRepo.findNewUser(projectId);
        model.addAttribute("users", users);

        //IssuePriority[] issuePriorities = IssuePriority.values();
        model.addAttribute("issuePriorities", IssuePriority.values());

        //IssueStatus[] issueStatuses = IssueStatus.values();
        model.addAttribute("issueStatuses", IssueStatus.values());

        isEditor = false;
        if (project.isOwner(user) || project.isAdmin(user)) {
            isEditor = true;
        }
        model.addAttribute("isEditor", isEditor);

        return "/workspace";
    }

    @PostMapping("/addissue")
    public String addIssue(@AuthenticationPrincipal User user, @RequestParam Long projectId,
                                 @RequestParam String issueName, @RequestParam String issueDescription,
                                 @RequestParam IssuePriority issuePriority, @RequestParam IssueStatus issueStatus,
                                 @RequestParam User executorId) {
        Project project = projectRepo.findById(projectId).get();

        if (!issueName.isEmpty() && !issueDescription.isEmpty()) {
            Issue issue = new Issue(issueName, issueDescription, user, executorId, project, issuePriority, issueStatus);
            project.getIssues().add(issue);
            projectRepo.save(project);
        }

        return "redirect:/workspace?projectId=" + projectId;
    }

    @PostMapping("/addProjectUser")
    public String addProjectUser(@AuthenticationPrincipal User user,
                                 @RequestParam Long userid,
                                 @RequestParam Long projectId) {
        User newUser = userRepo.findById(userid).get();
        Project project = projectRepo.findById(projectId).get();
        project.getUsers().add(newUser);
        projectRepo.save(project);
        newUser.getProjectsUser().add(project);
        userRepo.save(newUser);

        return "redirect:/workspace?projectId=" + projectId;
    }

    @GetMapping("/frombacklogtosprint")
    public String frombacklogtosprint(@AuthenticationPrincipal User user,
                                      @RequestParam Long issueid) {
        Optional<Issue> optionalIssue = issueRepo.findById(issueid);
        Long projectId = null;
        if (optionalIssue.isPresent()) {
            Issue issue = optionalIssue.get();
            issue.setIsBackLog(false);
            issueRepo.save(issue);
            projectId = issue.getProject().getId();
            return "redirect:/workspace?projectId=" + projectId;
        }

        return "redirect:/home";//если нет проектаid, то переход на общую страницу с проектами
    }

    @GetMapping("/fromsprinttobacklog")
    public String fromsprinttobacklog(@AuthenticationPrincipal User user,
                                      @RequestParam Long issueid) {
        Optional<Issue> optionalIssue = issueRepo.findById(issueid);
        Long projectId = null;
        if (optionalIssue.isPresent()) {
            Issue issue = optionalIssue.get();
            issue.setIsBackLog(true);
            issueRepo.save(issue);
            projectId = issue.getProject().getId();
            return "redirect:/workspace?projectId=" + projectId;
        }

        return "redirect:/home";//если нет проектаid, то переход на общую страницу с проектами
    }


}
