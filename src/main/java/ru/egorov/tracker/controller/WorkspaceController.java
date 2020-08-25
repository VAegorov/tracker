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
    public String inWorkspace(@AuthenticationPrincipal User user, @RequestParam Long projectid, Model model) {
        Project project = projectRepo.findById(projectid).get();
        //Project project1 = project.get();
        //Long idp = project.get().getId();

        model.addAttribute("project", project);

        Iterable<User> users = userRepo.findAll();
        model.addAttribute("users", users);

        return "/workspace";
    }

    /*@GetMapping("/workspace")
    public String homeWorkspace(@AuthenticationPrincipal User user, @RequestParam Long projectid, Model model) {

        return "workspace";
    }*/

    @PostMapping("/addProjectUser")
    public String addProjectUser(@AuthenticationPrincipal User user, @RequestParam Long userid, @RequestParam Long projectid,
                                 Model model) {
        User newUser = userRepo.findById(userid).get();
        Project project = projectRepo.findById(projectid).get();
        project.getUsers().add(newUser);
        projectRepo.save(project);
        newUser.getProjects().add(project);
        userRepo.save(newUser);

        model.addAttribute("project", project);

        Iterable<User> users = userRepo.findAll();
        model.addAttribute("users", users);

        return "/workspace";
    }

    /*@GetMapping
    public String workspace() {
        return null;
    }*/
}
