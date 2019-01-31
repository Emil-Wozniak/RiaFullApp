package org.ria.ifzz.RiaApp.services;

import org.ria.ifzz.RiaApp.domain.User;
import org.ria.ifzz.RiaApp.exceptions.UserNameAlreadyExistsException;
import org.ria.ifzz.RiaApp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    /**
     * @param newUser Instance of User class
     * @return new User object or ExceptionHandler if username already exists
     */
    public User saveUser(User newUser) {

        try{
            newUser.setPassword(bCryptPasswordEncoder.encode(newUser.getPassword()));
            //Username has to be unique (exception)
            newUser.setUsername(newUser.getUsername());
            // Make sure that password and confirmPassword match
            // We don't persist or show the confirmPassword
            newUser.setConfirmPassword("");
            return userRepository.save(newUser);
        } catch (Exception ex) {
            throw new UserNameAlreadyExistsException("Username '" + newUser.getUsername() + "' already exists");

        }
    }
}
