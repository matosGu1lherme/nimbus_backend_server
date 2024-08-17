package com.nimbus.nimbusWebServer.services;

import com.nimbus.nimbusWebServer.implementation.UserDetailsImpl;
import com.nimbus.nimbusWebServer.models.UserModel;
import com.nimbus.nimbusWebServer.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class UserDetailsImplService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    /*
        loadUserByUsername é usado pelo spring e é reponsavel por retornar um UserDetails
        com base no nome de usuario fornecido
    */
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserModel userModel = userRepository.findByEmail(username).orElseThrow(() -> new RuntimeException(("Usuario não encontrado.")));
        return new UserDetailsImpl(userModel);
    }
}
