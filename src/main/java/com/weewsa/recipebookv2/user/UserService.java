package com.weewsa.recipebookv2.user;

import com.weewsa.recipebookv2.authenticate.JWTService;
import com.weewsa.recipebookv2.authenticate.dto.RegisterRequest;
import com.weewsa.recipebookv2.user.exception.UserNotFound;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public void edit(String tokenLogin, RegisterRequest parameters) throws UserNotFound {
        var foundUser = userRepository.findByLogin(tokenLogin);

        if (foundUser.isEmpty()) {
            throw new UserNotFound("User not found");
        }

        var user = foundUser.get();
        user.setLogin(parameters.getLogin());
        user.setName(parameters.getName());
        user.setPassword(passwordEncoder.encode(parameters.getPassword()));
        user.setAboutMe(parameters.getAboutMe());

        userRepository.save(user);
    }
}
