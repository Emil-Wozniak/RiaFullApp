package org.ria.ifzz.RiaApp.services;

import org.ria.ifzz.RiaApp.models.User.User;
import org.ria.ifzz.RiaApp.repositories.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CustomUserDetailsService
//        implements UserDetailsService
{

//    private final UserRepository userRepository;
//
//    public CustomUserDetailsService(UserRepository userRepository) {
//        this.userRepository = userRepository;
//    }
//
//    @Override
//    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        User user = userRepository.findByUsername(username);
//        if (user == null)  throw new UsernameNotFoundException("User not found");
//        return user;
//    }
//
//    @Transactional
//    public User loadUserById(Long id) {
//        User user = userRepository.getById(id);
//        if (user == null) throw  new UsernameNotFoundException("User not found");
//        return user;
//    }
}
