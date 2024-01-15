package com.restapiblog.restapiblog.serviceImpl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.restapiblog.restapiblog.Dto.UserDto;
import com.restapiblog.restapiblog.Role;
import com.restapiblog.restapiblog.model.Users;
import com.restapiblog.restapiblog.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserDetailsService {

    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }



   @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username)
                .orElseThrow(()-> new UsernameNotFoundException("username not found"));

    }

    public Users saveUser(UserDto userDto) {
        Users user = new ObjectMapper().convertValue(userDto, Users.class);
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        user.setUserRole(Role.USER);
        return userRepository.save(user);
    }
}
