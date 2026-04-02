package app.domain.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import app.domain.Exception.NotFoundException;
import app.domain.models.User;
import app.domain.ports.UserPort;

@Service
public class FindUser {
    private UserPort userPort;
     
    @Autowired
    public FindUser(UserPort userPort){
        this.userPort = userPort;
    }

    public User findByDocument(String document) throws NotFoundException{
        User user = userPort.findByDocument(document);
        if(user == null){
            throw new NotFoundException("Usuario no encontrado");
        }

        return user;
    }

    public List<User> findAll(){
        return userPort.findAll();
    }
}
