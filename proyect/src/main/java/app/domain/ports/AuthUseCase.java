package app.domain.ports;

import app.domain.Exception.BussinesException;

public interface AuthUseCase {
    String login(String username, String password) throws BussinesException;
}
