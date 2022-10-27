package com.example.projeto.bootstrap;


import com.example.projeto.usermanagement.models.Role;
import com.example.projeto.usermanagement.models.User;
import com.example.projeto.usermanagement.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * Spring will load and execute all components that implement the interface
 * CommandLinerunner on startup, so we will use that as a way to bootstrap some
 * data for testing purposes.
 * <p>
 * In order to enable this bootstraping make sure you activate the spring
 * profile "bootstrap" in application.properties
 *
 * @author Group 1
 *
 */
@Component
@Profile("bootstrap")
public class UserBootstrapper implements CommandLineRunner {

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private PasswordEncoder encoder;

    @Override
    public void run(String... args) throws Exception {
        // admin
        if (userRepo.findByUsername("admin1@mail.com").isEmpty()) {
            User admin1 = new User("admin1@mail.com", encoder.encode("admin1"));
            admin1.addAuthority(new Role(Role.ADMIN));
            userRepo.save(admin1);
        }

        if (userRepo.findByUsername("admin2@mail.com").isEmpty()) {
            User admin2 = new User("admin2@mail.com", encoder.encode("admin2"));
            admin2.addAuthority(new Role(Role.ADMIN));
            userRepo.save(admin2);
        }

        if (userRepo.findByUsername("admin3@mail.com").isEmpty()) {
            User admin3 = new User("admin3@mail.com", encoder.encode("admin3"));
            admin3.addAuthority(new Role(Role.ADMIN));
            userRepo.save(admin3);
        }

        // registered customer
        if (userRepo.findByUsername("Pedro@mail.com").isEmpty()) {
            User reg1 = new User("Pedro@mail.com", encoder.encode("password1"));
            reg1.addAuthority(new Role(Role.REGISTERED));
            userRepo.save(reg1);
        }

        if (userRepo.findByUsername("Diogo@mail.com").isEmpty()) {
            User reg2 = new User("Diogo@mail.com", encoder.encode("password2"));
            reg2.addAuthority(new Role(Role.REGISTERED));
            userRepo.save(reg2);
        }

        if (userRepo.findByUsername("Tiago@mail.com").isEmpty()) {
            User reg3 = new User("Tiago@mail.com", encoder.encode("password3"));
            reg3.addAuthority(new Role(Role.REGISTERED));
            userRepo.save(reg3);
        }

        if (userRepo.findByUsername("David@mail.com").isEmpty()) {
            User reg4 = new User("David@mail.com", encoder.encode("password4"));
            reg4.addAuthority(new Role(Role.REGISTERED));
            userRepo.save(reg4);
        }

        if (userRepo.findByUsername("Soares@mail.com").isEmpty()) {
            User reg5 = new User("Soares@mail.com", encoder.encode("password5"));
            reg5.addAuthority(new Role(Role.REGISTERED));
            userRepo.save(reg5);
        }

        if (userRepo.findByUsername("Oliveira@mail.com").isEmpty()) {
            User reg6 = new User("Oliveira@mail.com", encoder.encode("password6"));
            reg6.addAuthority(new Role(Role.REGISTERED));
            userRepo.save(reg6);
        }

        // moderator
        if (userRepo.findByUsername("Rodrigues@mail.com").isEmpty()) {
            User mod1 = new User("Rodrigues@mail.com", encoder.encode("mod1"));
            mod1.addAuthority(new Role(Role.MODERATOR));
            userRepo.save(mod1);
        }

        if (userRepo.findByUsername("Ferreira@mail.com").isEmpty()) {
            User mod2 = new User("Ferreira@mail.com", encoder.encode("mod2"));
            mod2.addAuthority(new Role(Role.MODERATOR));
            userRepo.save(mod2);
        }

        if (userRepo.findByUsername("Alex@mail.com").isEmpty()) {
            User mod3 = new User("Alex@mail.com", encoder.encode("mod3"));
            mod3.addAuthority(new Role(Role.MODERATOR));
            userRepo.save(mod3);
        }
    }
}
