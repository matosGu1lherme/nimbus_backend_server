package com.nimbus.nimbusWebServer.services;

import com.nimbus.nimbusWebServer.dtos.CreateUserDto;
import com.nimbus.nimbusWebServer.dtos.LoginUserDto;
import com.nimbus.nimbusWebServer.implementation.UserDetailsImpl;
import com.nimbus.nimbusWebServer.models.user.User;
import com.nimbus.nimbusWebServer.models.user.UserAddress;
import com.nimbus.nimbusWebServer.repositories.UserRepository;
import com.nimbus.nimbusWebServer.config.SecurityConfig;
import com.nimbus.nimbusWebServer.security.roles.Role;
import com.nimbus.nimbusWebServer.security.roles.RoleName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class UserService {

    private AuthenticationManager authenticationManager;
    private UserRepository userRepository;
    private SecurityConfig securityConfig;
    private RefreshTokenService refreshTokenService;

    public UserService(
            AuthenticationManager authenticationManager,
            UserRepository userRepository,
            SecurityConfig securityConfig,
            RefreshTokenService refreshTokenService
    ) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.securityConfig = securityConfig;
        this.refreshTokenService = refreshTokenService;
    }


    public String authenticateUser(LoginUserDto loginUserDto) {
        var usernamePassword = new UsernamePasswordAuthenticationToken(loginUserDto.email(), loginUserDto.password());

        var auth = this.authenticationManager.authenticate(usernamePassword);

        UserDetailsImpl userDetails = ((UserDetailsImpl) auth.getPrincipal());

        return refreshTokenService.criarRefreshToken(userDetails).getToken();
    }

    @Transactional
    public void createUser(CreateUserDto createUserDto) {
        User newUser = User.builder()
                .nome(createUserDto.nome())
                .email(createUserDto.email())
                .password(securityConfig.passwordEncoder().encode(createUserDto.password()))
                .cpf(createUserDto.cpf())
                .data_nascimento(createUserDto.data_nascimento())
                .roles(List.of(Role.builder().name(createUserDto.role()).build()))
                .build();

        vincularEndereco(newUser, createUserDto);

        userRepository.save(newUser);
    }

    @Transactional
    public void createStoreUser(CreateUserDto createUserDto) {
        User newUser = User.builder()
                .nome(createUserDto.nome())
                .email(createUserDto.email())
                .password(securityConfig.passwordEncoder().encode(createUserDto.password()))
                .cpf(createUserDto.cpf())
                .data_nascimento(createUserDto.data_nascimento())
                .roles(List.of(Role.builder().name(RoleName.ROLE_CUSTOMER).build()))
                .build();

        vincularEndereco(newUser, createUserDto);

        userRepository.save(newUser);
    }

    private void vincularEndereco(User newUser, CreateUserDto createUserDto) {
        var enderecoDto = createUserDto.endereco();

        UserAddress endereco = new UserAddress();
        endereco.setUser(newUser);
        endereco.setLogradouro(enderecoDto.logradouro());
        endereco.setNumero(enderecoDto.numero());
        endereco.setBairro(enderecoDto.bairro());
        endereco.setCidade(enderecoDto.cidade());
        endereco.setCep(enderecoDto.cep());
        endereco.setPais(enderecoDto.pais() != null ? enderecoDto.pais() : "Brasil");
        endereco.setPrincipal(true);

        newUser.getEnderecos().add(endereco);
    }
}