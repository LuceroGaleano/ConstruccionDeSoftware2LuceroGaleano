package app.domain.services;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import app.domain.Exception.BussinesException;
import app.domain.Exception.NotFoundException;
import app.domain.models.BankAccount;
import app.domain.models.Customer;
import app.domain.models.User;
import app.domain.models.enums.RolUser;
import app.domain.ports.BankAccountPort;
import app.domain.ports.CustomerPort;

@Service
public class FindBankAccount {
    private final BankAccountPort bankAccountPort;
    private final CustomerPort customerPort;

    @Autowired
    public FindBankAccount(BankAccountPort bankAccountPort, CustomerPort customerPort){
        this.bankAccountPort = bankAccountPort;
        this.customerPort = customerPort;
    }

    public BankAccount findById(UUID id) throws NotFoundException{
        BankAccount bankAccount = bankAccountPort.findById(id);
        if(bankAccount == null){
            throw new NotFoundException("Cuenta bancaria no encontrada");
        }
        return bankAccount;
    }

    public BankAccount findByAccountNumber(int accountNumber, User user) throws NotFoundException, BussinesException{
        BankAccount bankAccount = bankAccountPort.findByAccountNumber(accountNumber);
        if(bankAccount == null){
            throw new NotFoundException("Cuenta bancaria no encontrada");
        }

        //Si es Customer, validar que este viendo sus productos
        if(!bankAccount.getCustomerOwner().getDocument().equals(user.getDocument()) &&
        (user.getSystemRole().equals(RolUser.PersonCustomerUser) ||
        user.getSystemRole().equals(RolUser.CorporateCustomerUser))){
            throw new BussinesException("No puedes ver esta cuenta bancarias, no eres dueño de la cuenta bancaria");
        }
        return bankAccount;
    }

    public List<BankAccount> findByCustomerOwner(String customerDocument, User user) throws NotFoundException, BussinesException {
        Customer customer = customerPort.findByDocument(customerDocument);
        if (customer == null) {
            throw new NotFoundException("Cliente no encontrado");
        }

        // Si es CorporateEmployee o CorporateSupervisor validar que sea su empresa
        if (user.getSystemRole().equals(RolUser.CorporateEmployee) ||
                user.getSystemRole().equals(RolUser.CorporateSupervisor)) {
            if (!customer.getDocument().equals(user.getCustomer().getDocument())) {
                throw new BussinesException("No puedes ver estas cuentas, la empresa no es la titular");
            }
        }

        return bankAccountPort.findByCustomerOwner(customer);
    }
    }