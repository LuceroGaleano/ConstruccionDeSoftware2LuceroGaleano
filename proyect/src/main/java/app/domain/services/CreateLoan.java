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
import app.domain.ports.LoanPort;

@Service
public class CreateLoan {
    private LoanPort loanPort;

    @Autowired
    public CreateLoan(LoanPort loanPort){
        this.loanPort = loanPort;
    }

    public void createLoan(Loan loan) throws BussinesException{
        Customer customerOwner = loan.getCustomerOwner();
        BankAccount bankAccount = loan.getDisburseAccount();

        //Validar que no se repita el id
        if(loanPort.existsById(loan.getProductID())){
            throw new BussinesException("Ya existe una cuent abancaria con el mismo id");
        }

        //Validamos que el cliente solicitante si exista
        if(customerOwner == null){
            throw new BussinesException("Cliente no encontrado");
        }

        //Validamos que el cliente este activo
        if(customerOwner.getCustomerStatus() != CustomerStatus.Active){
            throw new BussinesException("El cliente no puede solicitar prestamos");
        }

        //Validamos que la cuenta desembolso exista
        if(bankAccount == null){
            throw new BussinesException("Cuenta desembolso no encontrada");
        }

        //Validamos que la cuenta de desemboloso este activa
        if(bankAccount.getAccountStatus() != AccountStatus.Active){
            throw new BussinesException("La cuenta de desembolso no esta activa");
        }
        
        //Validamos que el propietario de la cuenta de desembolos sea el cliente solicitante
        if(!bankAccount.getCustomerOwner().equals(customerOwner)){
            throw new BussinesException("La cuenta de desembolso no pertenece el cliente solicitante");
        }

        loan.setCustomerOwner(customerOwner);
        loan.setProductCategory(ProductCategory.Loan);
        loan.setLoanStatus(LoanStatus.Requested);
        loan.setCreateDate(new Date(System.currentTimeMillis()));
        loanPort.save(loan);
        //!Debe ir a aprobacion
    }
}
