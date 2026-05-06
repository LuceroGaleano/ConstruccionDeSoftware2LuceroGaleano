package app.application.usecases;

import org.springframework.stereotype.Service;

import app.domain.Exception.BussinesException;
import app.domain.services.LoginUser;

@Service
public class AuthUseCase implements app.domain.ports.AuthUseCase{
    private final LoginUser loginUser;

    public AuthUseCase(LoginUser loginUser){
        this.loginUser = loginUser;
    }

    @Override
    public String login(String username, String password) throws BussinesException {
        return loginUser.execute(username, password);
    }
}
