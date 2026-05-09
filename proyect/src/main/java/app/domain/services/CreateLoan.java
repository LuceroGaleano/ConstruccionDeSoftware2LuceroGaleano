package app.domain.services;

import java.sql.Date;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import app.domain.Exception.BussinesException;
import app.domain.models.BankAccount;
import app.domain.models.Bitacora;
import app.domain.models.Customer;
import app.domain.models.Loan;
import app.domain.models.User;
import app.domain.models.enums.AccountStatus;
import app.domain.models.enums.CustomerStatus;
import app.domain.models.enums.LoanStatus;
import app.domain.models.enums.OperationBitacora;
import app.domain.models.enums.ProductCategory;
import app.domain.models.enums.RolUser;
import app.domain.ports.BankAccountPort;
import app.domain.ports.BitacoraPort;
import app.domain.ports.CustomerPort;
import app.domain.ports.LoanPort;

@Service
public class CreateLoan {
    private final LoanPort loanPort;
    private final CustomerPort customerPort;
    private final BankAccountPort bankAccountPort;
    private final BitacoraPort bitacoraPort;

    @Autowired
    public CreateLoan(LoanPort loanPort, CustomerPort customerPort, BankAccountPort bankAccountPort, BitacoraPort bitacoraPort){
        this.loanPort = loanPort;
        this.customerPort = customerPort;
        this.bankAccountPort = bankAccountPort;
        this.bitacoraPort = bitacoraPort;
    }

    public void createLoan(Loan loan, User user) throws BussinesException{
        Customer customerOwner = loan.getCustomerOwner();
        // Validamos que el cliente solicitante exista y lo obtenemos completo desde la BD
        Customer customer = customerPort.findByDocument(customerOwner.getDocument());
        if(customer == null){
            throw new BussinesException("Cliente no encontrado");
        }

        // Validamos que el cliente esté activo
        if(customer.getCustomerStatus() != CustomerStatus.Active){
            throw new BussinesException("El cliente no puede solicitar préstamos");
        }

        // Validamos que la cuenta de desembolso exista
        if(loan.getDisburseAccount() == null){
            throw new BussinesException("Cuenta de desembolso no encontrada");
        }

        // Obtenemos la cuenta de desembolso completa desde la BD
        BankAccount bankAccount = bankAccountPort.findByAccountNumber(loan.getDisburseAccount().getAccountNumber());
        if(bankAccount == null){
            throw new BussinesException("Cuenta de desembolso no encontrada");
        }

        // Validamos que la cuenta de desembolso esté activa
        if(bankAccount.getAccountStatus() != AccountStatus.Active){
            throw new BussinesException("La cuenta de desembolso no esta activa");
        }

        // Validamos que el propietario de la cuenta sea el cliente solicitante
        if(!bankAccount.getCustomerOwner().getDocument().equals(customer.getDocument())){
            throw new BussinesException("La cuenta de desembolso no pertenece al cliente solicitante");
        }

        if(!user.getDocument().equals(customer.getDocument()) && (user.getSystemRole().equals(RolUser.PersonCustomerUser) || user.getSystemRole().equals(RolUser.CorporateCustomerUser))){
            throw new BussinesException("No puedes solicitar un prestamo para otro cliente");
        }

        loan.setApproved(true);
        loan.setProductName("Préstamo #" + (int)(Math.random() * 900000 + 100000) + " para " + customer.getFullName());
        loan.setCustomerOwner(customer);
        loan.setDisburseAccount(bankAccount);
        loan.setProductCategory(ProductCategory.Loan);
        loan.setLoanStatus(LoanStatus.Requested);
        loan.setCreateDate(new Date(System.currentTimeMillis()));
        loanPort.save(loan);

        Map<String, Object> detailData = Map.of(
            "requestedAmount: ", loan.getRequestedAmount(),
            "InterestRate", loan.getInterestRate(),
            "previousState", loan.getLoanStatus()
        );


        //Bitacora
        Bitacora bitacora = new Bitacora();
        bitacora.setOperationType(OperationBitacora.CreationLoan);
        bitacora.setUserDocument(user.getDocument());
        bitacora.setRolUser(user.getSystemRole());
        bitacora.setProductId(loan.getId());
        bitacora.setDetailData(detailData);
        System.out.println("Guardando bitacora...");
        bitacoraPort.save(bitacora);
        System.out.println("Bitacora guardada!");
    }
}