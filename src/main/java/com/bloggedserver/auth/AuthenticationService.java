package com.bloggedserver.auth;

import com.bloggedserver.user.User;
import com.bloggedserver.user.UserJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserJpaRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthenticationResponse register(GeneralAuthenticationOrRegisterRequest request) {
        User user = User.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode((request.getPassword())))
                .build();
        System.out.println(repository.findByUsername(request.getUsername()).isEmpty());
        if (repository.findByUsername(request.getUsername()).isEmpty()){
            repository.save(user);
        } else {
            throw new DuplicateUserException();
        }
        String jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .username(user.getUsername())
                .build();
    }

    public AuthenticationResponse authenticate(GeneralAuthenticationOrRegisterRequest request) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(),
                request.getPassword()));
        //If user does not exist, the above authenticate function will throw the error. So we dont need to
        //handle the error from the repository.
        User user = repository.findByUsername(request.getUsername()).orElseThrow();
        String jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .username(user.getUsername())
                .build();
    }
}
