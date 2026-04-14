package app.domain.services;

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
    private CustomerPort customerPort;
    private BankAccountPort bankAccountPort;

    @Autowired
    public CreateBankAccount(CustomerPort customerPort, BankAccountPort bankAccountPort){
        this.customerPort = customerPort;
        this.bankAccountPort = bankAccountPort;
    }

    public void createBankAccount(BankAccount bankAccount) throws BussinesException{
        Customer customer = customerPort.findByDocument(bankAccount.getCustomerOwner().getIdentification());
        //Validar que no se repita el id
        if(bankAccountPort.existsById(bankAccount.getProductID())){
            throw new BussinesException("Ya existe una cuenta bancaria con el mismo id");
        }
        
        //Validar que exista el cliente
        if(customer == null){
            throw new BussinesException("No existe el cliente");
        }

        //Validar que el cliente este activo
        if(customer.getCustomerStatus() != CustomerStatus.Active){
            throw new BussinesException("El cliente no puede crear una nueva cuenta");
        }

        //Validar que no se repita el número de cuenta
        if(bankAccountPort.existsByNumber(bankAccount.getAccountNumber())){
            throw new BussinesException("Ya existe una cuenta con el mismo número");
        }

        //Enviar rol segun el tipo de customer
        if(customer.getRolCustomer() == RolCustomer.CorporateCustomer){
            bankAccount.setAccountType(AccountType.Current);
        } else{
            bankAccount.setAccountType(AccountType.Saving);
        }

        bankAccount.setProductCategory(ProductCategory.BankAccount);
        bankAccount.setCustomerOwner(customer);
        bankAccount.setAccountStatus(AccountStatus.Active);
        bankAccount.setOpeningDate(new Date(System.currentTimeMillis()));
        bankAccountPort.save(bankAccount);
    }
}
