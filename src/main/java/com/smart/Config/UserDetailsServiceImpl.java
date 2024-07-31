package com.smart.Config;

import com.smart.Dao.UserRepository;
import com.smart.Models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.getUserByUserName(username);

        if (user == null) {
            throw new UsernameNotFoundException("Could not found user");
        }

        CustomUserDetails userDetails = new CustomUserDetails(user);

        return userDetails;
    }
}
