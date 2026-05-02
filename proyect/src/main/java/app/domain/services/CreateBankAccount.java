package app.domain.services;

import java.math.BigDecimal;
import java.sql.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import app.domain.Exception.BussinesException;
import app.domain.models.BankAccount;
import app.domain.models.Customer;
import app.domain.models.enums.AccountStatus;
import app.domain.models.enums.AccountType;
import app.domain.models.enums.CustomerStatus;
import app.domain.models.enums.ProductCategory;
import app.domain.models.enums.RolCustomer;
import app.domain.ports.BankAccountPort;
import app.domain.ports.CustomerPort;

@Service
public class CreateBankAccount {
    private final CustomerPort customerPort;
    private final BankAccountPort bankAccountPort;

    @Autowired
    public CreateBankAccount(CustomerPort customerPort, BankAccountPort bankAccountPort){
        this.customerPort = customerPort;
        this.bankAccountPort = bankAccountPort;
    }

    public void createBankAccount(BankAccount bankAccount) throws BussinesException{


        // Validar que exista el cliente y obtenerlo completo desde la BD
        Customer customer = customerPort.findByDocument(bankAccount.getCustomerOwner().getDocument());
        if(customer == null){
            throw new BussinesException("No existe el cliente");
        }

        // Validar que el cliente este activo
        if(customer.getCustomerStatus() != CustomerStatus.Active){
            throw new BussinesException("El cliente no puede crear una nueva cuenta");
        }

        // Generar número de cuenta único
        int accountNumber;
        do {
            accountNumber = (int)(Math.random() * 900000000) + 100000000; // 9 dígitos
        } while(bankAccountPort.existsByAccountNumber(accountNumber));


        // Asignar tipo de cuenta según el rol del cliente
        if(customer.getRolCustomer() == RolCustomer.CorporateCustomer){
            bankAccount.setAccountType(AccountType.Current);
        } else if(customer.getRolCustomer() == RolCustomer.PersonCustomer){
            bankAccount.setAccountType(AccountType.Saving);
        } else{
            throw new BussinesException("El cliente no tiene un rol válido para crear una cuenta bancaria");
        }

        
        bankAccount.setApproved(false);
        bankAccount.setAccountNumber(accountNumber);
        bankAccount.setProductName("Cuenta" + bankAccount.getAccountNumber() + " de " + customer.getFullName());
        bankAccount.setCurrentBalance(BigDecimal.ZERO);
        bankAccount.setProductCategory(ProductCategory.BankAccount);
        bankAccount.setCustomerOwner(customer);
        bankAccount.setAccountStatus(AccountStatus.Active);
        bankAccount.setOpeningDate(new Date(System.currentTimeMillis()));
        bankAccountPort.save(bankAccount);
    }
}