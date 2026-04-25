package app.domain.ports;

import java.util.List;

import app.domain.models.User;

public interface UserPort {
    public boolean existsByDocument(String identification);
    public boolean existsByUserName(String userName);
    public boolean existsByEmail(String email);

    public User findByDocument(String identification);
    public User findByUserName(String userName);
    public List<User> findAll();


    public void save(User user);
    public void update(User user);
    public void deleteByDocument(String identification);
}
