package com.blog.init;

import com.blog.entities.User;
import com.blog.repositories.RoleRepository;
import com.blog.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@Order(value = 2)
public class UsersInit implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;

    @Autowired
    public UsersInit(UserRepository userRepository, PasswordEncoder passwordEncoder, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
    }

    @Override
    public void run(String... args) throws Exception {

        if (this.userRepository.count() == 0) {

            User admin = new User();
            admin.setName("Vladimir Velikov");
            admin.setEmail("admin@gmail.com");
            admin.setPassword(this.passwordEncoder.encode("vladimir123"));
            admin.setRoles(Set.of(this.roleRepository.findRoleByName("ADMIN")));

            this.userRepository.save(admin);
        }
    }
}
