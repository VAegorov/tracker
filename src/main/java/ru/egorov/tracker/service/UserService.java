package ru.egorov.tracker.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.egorov.tracker.repos.ProjectRepo;
import ru.egorov.tracker.repos.UserRepo;

@Service
public class UserService implements UserDetailsService {
    //@Autowired
    private final UserRepo userRepo;
    //private final ProjectRepo projectRepo;

    public UserService(UserRepo userRepo) {
        this.userRepo = userRepo;
        //this.projectRepo = projectRepo;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepo.findByUsername(username);
    }
    /*@Override
    public UserDetails loadProjectById(Long id) throws UsernameNotFoundException {
        return projectRepo.findById(id);
    }*/




}