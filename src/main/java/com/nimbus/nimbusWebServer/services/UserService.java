package com.nimbus.nimbusWebServer.services;

import com.nimbus.nimbusWebServer.dtos.CreateUserDto;
import com.nimbus.nimbusWebServer.dtos.LoginUserDto;
import com.nimbus.nimbusWebServer.implementation.UserDetailsImpl;
import com.nimbus.nimbusWebServer.models.user.User;
import com.nimbus.nimbusWebServer.repositories.UserRepository;
import com.nimbus.nimbusWebServer.security.configure.SecurityConfiguration;
import com.nimbus.nimbusWebServer.security.roles.Role;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private AuthenticationManager authenticationManager;
    private AccessTokenService jwtTokenService;
    private UserRepository userRepository;
    private SecurityConfiguration securityConfiguration;


    public UserService(
            AuthenticationManager authenticationManager,
            AccessTokenService jwtTokenService,
            UserRepository userRepository,
            SecurityConfiguration securityConfiguration,
            RefreshTokenService refreshTokenService
    ) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenService = jwtTokenService;
        this.userRepository = userRepository;
        this.securityConfiguration = securityConfiguration;
        this.refreshTokenService = refreshTokenService;
    }

    private RefreshTokenService refreshTokenService;

    // Método responsável por autenticar um usuário e retornar um token JWT
    public String authenticateUser(LoginUserDto loginUserDto) {
        // Cria um objeto de autenticação com o email e a senha do usuário
        var usernamePassword = new UsernamePasswordAuthenticationToken(loginUserDto.email(), loginUserDto.password());

        // Autentica o usuário com as credenciais fornecidas
        var auth = this.authenticationManager.authenticate(usernamePassword);

        // Obtém o objeto UserDetails do usuário autenticado
        UserDetailsImpl userDetails = ((UserDetailsImpl) auth.getPrincipal());

        // Gera um token JWT para o usuário autenticado
        return refreshTokenService.criarRefreshToken(userDetails).getToken();
    }

    // Método responsável por criar um usuário
    public void createUser(CreateUserDto createUserDto) {

        // Cria um novo usuário com os dados fornecidos
        User newUser = User.builder()
                .nome(createUserDto.nome())
                .email(createUserDto.email())
                .password(securityConfiguration.passwordEncoder().encode(createUserDto.password()))
                .cpf(createUserDto.cpf())
                .data_nascimento(createUserDto.data_nascimento())
                .roles(List.of(Role.builder().name(createUserDto.role()).build()))
                .build();

        // Salva o novo usuário no banco de dados
        userRepository.save(newUser);
    }
}