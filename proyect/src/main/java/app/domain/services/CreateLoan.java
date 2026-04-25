package app.domain.services;

import java.sql.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import app.domain.Exception.BussinesException;
import app.domain.models.BankAccount;
import app.domain.models.Customer;
import app.domain.models.Loan;
import app.domain.models.enums.AccountStatus;
import app.domain.models.enums.CustomerStatus;
import app.domain.models.enums.LoanStatus;
import app.domain.models.enums.ProductCategory;
import app.domain.ports.CustomerPort;
import app.domain.ports.LoanPort;

@Service
public class CreateLoan {
    private final LoanPort loanPort;
    private final CustomerPort customerPort;

    @Autowired
    public CreateLoan(LoanPort loanPort, CustomerPort customerPort){
        this.loanPort = loanPort;
        this.customerPort = customerPort;
    }

    public void createLoan(Loan loan) throws BussinesException{
        Customer customerOwner = loan.getCustomerOwner();
        BankAccount bankAccount = loan.getDisburseAccount();

        //Validar que no se repita el id
        if(loanPort.existsById(loan.getId())){
            throw new BussinesException("Ya existe una cuenta bancaria con el mismo id");
        }

        //Validamos que el cliente solicitante si exista
        Customer customer = customerPort.findByDocument(customerOwner.getDocument());
        if(customer == null){
            throw new BussinesException("Cliente no encontrado");
        }

        //Validamos que el cliente esté activo
        if(customerOwner.getCustomerStatus() != CustomerStatus.Active){
            throw new BussinesException("El cliente no puede solicitar préstamos");
        }

        //Validamos que la cuenta de desembolso exista
        if(bankAccount == null){
            throw new BussinesException("Cuenta de desembolso no encontrada");
        }

        //Validamos que la cuenta de desembolso esté activa
        if(bankAccount.getAccountStatus() != AccountStatus.Active){
            throw new BussinesException("La cuenta de desembolso no esta activa");
        }
        
        //Validamos que el propietario de la cuenta de desembolso sea el cliente solicitante
        if(!bankAccount.getCustomerOwner().equals(customerOwner)){
            throw new BussinesException("La cuenta de desembolso no pertenece al cliente solicitante");
        }

        loan.setCustomerOwner(customerOwner);
        loan.setProductCategory(ProductCategory.Loan);
        loan.setLoanStatus(LoanStatus.Requested);
        loan.setCreateDate(new Date(System.currentTimeMillis()));
        loanPort.save(loan);
        //!Debe ir a aprobacion
    }
}
