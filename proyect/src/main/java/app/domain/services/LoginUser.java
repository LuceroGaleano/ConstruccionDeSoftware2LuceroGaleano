package app.domain.services;

import org.springframework.stereotype.Service;

import app.domain.Exception.BussinesException;
import app.domain.models.User;
import app.domain.ports.UserPort;
import app.infrastructure.security.JwtUtil;

@Service
public class LoginUser {
    private final UserPort userPort;
    private final JwtUtil jwtUtil;

    public LoginUser(UserPort userPort, JwtUtil jwUtil){
        this.userPort = userPort;
        this.jwtUtil = jwUtil;
    }

    public String execute(String userName, String password) throws  BussinesException{
        User user = userPort.findByUserName(userName);
        if(user == null){
            throw new BussinesException("Oh no! Haz ingresado mal algun dato");

        }
        if(!password.equals(user.getPassword())){
            throw new BussinesException("Oh no! Haz ingresado mal algun dato");
        }

        return jwtUtil.generateToken(user.getDocument(), user.getUserName(), user.getSystemRole().toString());
    }
}
