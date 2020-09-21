package ru.egorov.tracker.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.egorov.tracker.domain.Project;
import ru.egorov.tracker.domain.User;
import ru.egorov.tracker.domain.issue.Issue;
import ru.egorov.tracker.domain.issue.SubIssue;
import ru.egorov.tracker.repos.IssueRepo;
import ru.egorov.tracker.repos.ProjectRepo;
import ru.egorov.tracker.repos.SubIssueRepo;
import ru.egorov.tracker.repos.UserRepo;

import javax.transaction.Transactional;
import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

import static java.lang.Thread.currentThread;
import static java.lang.Thread.sleep;

@Controller
@RequestMapping("/admin")
@PreAuthorize("hasAuthority('ADMIN')")
public class AdminController {
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private ProjectRepo projectRepo;
    @Autowired
    private IssueRepo issueRepo;
    @Autowired
    private SubIssueRepo subIssueRepo;

    private User none;

    @GetMapping("/main")
    public String adminmain() {


        return "adminmain";
    }

    @GetMapping("/userlist")
    public String userlist(Model model) {
        model.addAttribute("users", userRepo.findAll());

        return "userlist";
    }

    @GetMapping("/userlist/{user}")
    @Transactional
    public String userdelete(@PathVariable User user) {
        none = userRepo.findByUsername("none");
        System.out.println("id none************ " + none.getId());
        Iterable<Issue> isCreatorIssues= issueRepo.findAllByCreatorId(user.getId());
        isCreatorIssues.forEach(issue -> issue.setCreator(none));
        issueRepo.saveAll(isCreatorIssues);
        Iterable<Issue> isExecutorIssues= issueRepo.findAllByExecutorId(user.getId());
        isExecutorIssues.forEach(issue -> issue.setExecutor(none));

        issueRepo.saveAll(isExecutorIssues);
        //перназначили где удаляемый owner
        user.getProjectsOwner().forEach(p -> p.setOwner(none));

        //перназначили где удаляемый admin
        user.getProjectsAdmin().forEach(p -> p.setAdmin(none));

        //переназначаем задачи и подзадачи
        Set<SubIssue> subIssuesUser = subIssueRepo.findByExecutorId(user.getId());//все подзадачи где удаляемый исполнитель во всех проектах
        subIssuesUser.forEach(i -> i.setExecutor(none));//исполнителем подзадач назначаем none

        Set<SubIssue> subIssuesUserIsCreator = subIssueRepo.findByCreatorId(user.getId());//все подзадачи где удаляемый автор во всех проектах
        subIssuesUserIsCreator.forEach(i -> i.setCreator(none));//создателем подзадач назначаем none*/

        subIssueRepo.saveAll(subIssuesUser);
        subIssueRepo.saveAll(subIssuesUserIsCreator);


        user.setProjectsAdmin(null);
        user.setProjectsOwner(null);
        user.setProjectsUser(null);
        user.setIssueCreator(null);
        user.setIssueExecutor(null);
        userRepo.delete(user);

        return "redirect:/admin/userlist";
    }

    @GetMapping("/projectlist")
    public String projectlist(Model model) {
        model.addAttribute("projects", projectRepo.findAll());

        return "projectlist";
    }

    @GetMapping("/projectlist/{project}")
    @Transactional
    public String projectdelete (@PathVariable Project project) {
        project.setOwner(null);
        project.setAdmin(null);
        Iterable<Issue> issues = issueRepo.findByProjectId(project.getId());
        System.out.println("Get issues!");
        issues.forEach(System.out::println);

        //issueRepo.deleteAll(issues);
        //project.getIssues().removeAll((Collection<Issue>) issues);
        project.getUsers().clear();
        //projectRepo.save(project);
        issues = project.getIssues();
        ((Set<Issue>) issues).clear();
        System.out.println("After clear!");
        issues.forEach(System.out::println);
        //projectRepo.save(project);
        projectRepo.delete(project);

        return "redirect:/admin/projectlist";
    }
}
