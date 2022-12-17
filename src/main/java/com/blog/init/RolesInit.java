package com.blog.init;

import com.blog.entities.Role;
import com.blog.repositories.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class RolesInit implements CommandLineRunner {

    private final RoleRepository roleRepository;

    @Autowired
    public RolesInit(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        if (roleRepository.count() == 0) {
            Role adminRole = new Role();
            adminRole.setName("ADMIN");

            Role userRole = new Role();
            userRole.setName("USER");

            this.roleRepository.saveAll(List.of(adminRole, userRole));
        }
    }
}
