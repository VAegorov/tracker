package ru.egorov.tracker.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.egorov.tracker.domain.Project;
import ru.egorov.tracker.domain.User;
import ru.egorov.tracker.repos.ProjectRepo;
import ru.egorov.tracker.repos.UserRepo;

import java.util.Optional;

@Controller
public class WorkspaceController {
    @Autowired
    private ProjectRepo projectRepo;

    @Autowired
    private UserRepo userRepo;

    @PostMapping("/workspace")
    public String inWorkspace(@AuthenticationPrincipal User user, @RequestParam Long id, Model model) {
        Optional<Project> project = projectRepo.findById(id);
        Project project1 = project.get();
        //Long idp = project.get().getId();

        model.addAttribute("project1", project1);

        Iterable<User> users = userRepo.findAll();
        model.addAttribute("users", users);

        return "/workspace";
    }

    /*@GetMapping
    public String workspace() {
        return null;
    }*/
}
