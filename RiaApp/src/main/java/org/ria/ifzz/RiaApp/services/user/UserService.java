package org.ria.ifzz.RiaApp.services.user;

import org.springframework.stereotype.Service;

@Service
public class UserService {
//
//    private final UserRepository userRepository;
//    private final BCryptPasswordEncoder bCryptPasswordEncoder;
//
//    public UserService(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
//        this.userRepository = userRepository;
//        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
//    }
//
//    /**
//     * @param newUser Instance of user class
//     * @return new user object or ExceptionHandler if username already exists
//     */
//    public user saveUser(user newUser) throws UserNameAlreadyExistsException {
//
//        try{
//            newUser.setPassword(bCryptPasswordEncoder.encode(newUser.getPassword()));
//            newUser.setUsername(newUser.getUsername());
//            newUser.setConfirmPassword("");
//            return userRepository.save(newUser);
//        } catch (Exception ex) {
//            throw new UserNameAlreadyExistsException("Username '" + newUser.getUsername() + "' already exists");
//        }
//    }
}
