package app.domain.ports;

import app.domain.models.User;

public interface UserPort {
    public boolean existisByDocument(String identification);
    public boolean existisUserName(String userName);
    public User findByDocument(String identification);
    public User findByUserName(String userName);
    public void save(User user);
}
