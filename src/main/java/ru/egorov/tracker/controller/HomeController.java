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


@Controller
public class HomeController {

    @Autowired
    private ProjectRepo projectRepo;

    @Autowired
    private UserRepo userRepo;

    @GetMapping("/")
    public String in() {

        return "in";
    }

    @PostMapping("/home")
    public String addProject(@AuthenticationPrincipal User user, @RequestParam String projectName, Model model) {
        if (!projectName.isEmpty()) {
            Project project = new Project(projectName, user);
            projectRepo.save(project);
            user.getProjectsOwner().add(project);
            userRepo.save(user);
        }

        return "redirect:/home";
    }

    @GetMapping("/home")
    public String home(@AuthenticationPrincipal User user, Model model) {

        Iterable<Project> ownerProjects = projectRepo.findByOwnerId(user.getId());
        model.addAttribute("ownerProjects", ownerProjects);

        Iterable<Project> adminProjects = projectRepo.findByAdminId(user.getId());
        model.addAttribute("adminProjects", adminProjects);

        Iterable<Project> userProjects = projectRepo.findAllWhereByIdUser(user.getId());
        model.addAttribute("userProjects", userProjects);

        return "home";
    }
}
