package app.domain.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import app.domain.Exception.BussinesException;
import app.domain.models.Customer;
import app.domain.models.User;
import app.domain.ports.CustomerPort;
import app.domain.ports.UserPort;

@Service
public class UpdateUser {
    private final UserPort userPort;
    private final CustomerPort customerPort;

    @Autowired
    public UpdateUser(UserPort userPort, CustomerPort customerPort) {
        this.userPort = userPort;
        this.customerPort = customerPort;
    }

    public void updateUser(User user) throws BussinesException {

        // Validar que el usuario exista
        if (!userPort.existsByDocument(user.getDocument())) {
            throw new BussinesException("No existe un usuario con ese documento");
        }

        // Obtener el usuario actual para comparar
        User existingUser = userPort.findByDocument(user.getDocument());

        // Validar userName solo si cambió
        if (user.getUserName() != null && !user.getUserName().equals(existingUser.getUserName())) {
            if (userPort.existsByUserName(user.getUserName())) {
                throw new BussinesException("Nombre de usuario ya existente");
            }
        }

        // Validar email solo si cambió
        if (user.getEmail() != null && !user.getEmail().equals(existingUser.getEmail())) {
            if (userPort.existsByEmail(user.getEmail())) {
                throw new BussinesException("Ya existe un usuario con ese email");
            }
        }

        // Si existe un cliente con la misma identificación, actualizar sus datos
        if (customerPort.existsByDocument(user.getDocument())) {
            Customer existingCustomer = customerPort.findByDocument(user.getDocument());
            if (existingCustomer != null) {
                existingCustomer.setFullName(user.getFullName());
                existingCustomer.setEmail(user.getEmail());
                existingCustomer.setPhone(user.getPhone());
                existingCustomer.setAddress(user.getAddress());
                customerPort.update(existingCustomer);
            }
        }
        userPort.update(user);
    }

    private boolean hasMatchingData(User user) {
        Customer customer = customerPort.findByDocument(user.getDocument());
        return (customer.getFullName().equals(user.getFullName()) &&
                customer.getDocument().equals(user.getDocument()) &&
                customer.getEmail().equals(user.getEmail()) &&
                customer.getPhone().equals(user.getPhone()) &&
                customer.getAddress().equals(user.getAddress()));
    }
}