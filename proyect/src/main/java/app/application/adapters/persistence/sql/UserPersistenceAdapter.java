package app.application.adapters.persistence.sql;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import app.application.adapters.persistence.sql.entities.CorporateCustomerEntity;
import app.application.adapters.persistence.sql.entities.CustomerEntity;
import app.application.adapters.persistence.sql.entities.PersonCustomerEntity;
import app.application.adapters.persistence.sql.entities.UserEntity;
import app.application.adapters.persistence.sql.repositories.CustomerRepository;
import app.application.adapters.persistence.sql.repositories.UserRepository;
import app.domain.models.CorporateCustomer;
import app.domain.models.PersonCustomer;
import app.domain.models.User;
import app.domain.models.enums.RolUser;
import app.domain.models.enums.UserStatus;
import app.domain.ports.UserPort;

@Service
public class UserPersistenceAdapter implements UserPort {
    private final UserRepository userRepository;
    private final CustomerRepository customerRepository;

    public UserPersistenceAdapter(UserRepository userRepository, CustomerRepository customerRepository) {
        this.userRepository = userRepository;
        this.customerRepository = customerRepository;
    }

    @Override
    public void save(User user) {
        userRepository.save(toEntity(user));
    }

    @Override
    public void update(User user) {
        UserEntity existingUser = userRepository.findByDocument(user.getDocument());
        if (existingUser != null) {
            existingUser.setFullName(user.getFullName());
            existingUser.setDocument(user.getDocument());
            existingUser.setEmail(user.getEmail());
            existingUser.setPhone(user.getPhone());
            existingUser.setAddress(user.getAddress());
            existingUser.setUserID(user.getUserID());
            existingUser.setSystemRole(user.getSystemRole() != null ? user.getSystemRole().toString() : null);
            existingUser.setUserStatus(user.getUserStatus() != null ? user.getUserStatus().toString() : null);
            existingUser.setUserName(user.getUserName());
            existingUser.setPassword(user.getPassword());
            existingUser.setCompany(user.getCompany());
            if (user.getCustomer() != null) {
                CustomerEntity customerEntity = customerRepository.findByDocument(user.getCustomer().getDocument());
                existingUser.setCustomer(customerEntity);
            }
            userRepository.save(existingUser);
        }
    }

    @Override
    public void deleteByDocument(String document) {
        userRepository.deleteByDocument(document);
    }

    @Override
    public boolean existsByDocument(String document) {
        return userRepository.existsByDocument(document);
    }

    @Override
    public boolean existsByUserName(String userName) {
        return userRepository.existsByUserName(userName);
    }

    @Override
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    public User findByDocument(String document) {
        return toModel(userRepository.findByDocument(document));
    }

    @Override
    public User findByUserName(String userName) {
        return toModel(userRepository.findByUserName(userName));
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll().stream()
                .map(this::toModel).collect(Collectors.toList());
    }

    private UserEntity toEntity(User user) {
        UserEntity e = new UserEntity();
        e.setFullName(user.getFullName());
        e.setDocument(user.getDocument());
        e.setEmail(user.getEmail());
        e.setPhone(user.getPhone());
        e.setAddress(user.getAddress());
        e.setUserID(user.getUserID());
        e.setSystemRole(user.getSystemRole() != null ? user.getSystemRole().toString() : null);
        e.setUserStatus(user.getUserStatus() != null ? user.getUserStatus().toString() : null);
        e.setUserName(user.getUserName());
        e.setPassword(user.getPassword());
        e.setCompany(user.getCompany());
        if (user.getCustomer() != null) {
            CustomerEntity customerEntity = customerRepository.findByDocument(user.getCustomer().getDocument());
            e.setCustomer(customerEntity);
        }
        return e;
    }

    private User toModel(UserEntity e) {
        if (e == null) return null;
        User user = new User();
        user.setFullName(e.getFullName());
        user.setDocument(e.getDocument());
        user.setEmail(e.getEmail());
        user.setPhone(e.getPhone());
        user.setAddress(e.getAddress());
        user.setUserID(e.getUserID());
        user.setSystemRole(RolUser.valueOf(e.getSystemRole()));
        user.setUserStatus(UserStatus.valueOf(e.getUserStatus()));
        user.setUserName(e.getUserName());
        user.setPassword(e.getPassword());
        user.setCompany(e.getCompany());
        if (e.getCustomer() instanceof PersonCustomerEntity pc) {
            PersonCustomer customer = new PersonCustomer();
            customer.setDocument(pc.getDocument());
            customer.setFullName(pc.getFullName());
            user.setCustomer(customer);
        } else if (e.getCustomer() instanceof CorporateCustomerEntity cc) {
            CorporateCustomer customer = new CorporateCustomer();
            customer.setDocument(cc.getDocument());
            customer.setFullName(cc.getFullName());
            user.setCustomer(customer);
        }
        return user;
    }
}