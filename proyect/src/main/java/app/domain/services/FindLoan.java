package app.domain.services;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import app.domain.Exception.BussinesException;
import app.domain.Exception.NotFoundException;
import app.domain.models.Customer;
import app.domain.models.Loan;
import app.domain.models.User;
import app.domain.models.enums.RolUser;
import app.domain.ports.CustomerPort;
import app.domain.ports.LoanPort;

@Service
public class FindLoan {
    private final LoanPort loanPort;
    private final CustomerPort customerPort;

    @Autowired
    public FindLoan(LoanPort loanPort, CustomerPort customerPort){
        this.loanPort = loanPort;
        this.customerPort = customerPort;
    }

    public Loan findById(UUID id) throws NotFoundException{
        Loan loan = loanPort.findById(id);
        if(loan == null){
            throw new NotFoundException("Prestamo no encontrado");
        }
        return loan;
    }

    public List<Loan> findByCustomer(String customerDocument, User user) throws NotFoundException{
        Customer customer = customerPort.findByDocument(customerDocument);
        if(customer == null){
            throw new NotFoundException("Cliente no encontrado");
        }

        // Si es PersonCustomerUser o CorporateCustomerUser validar con user.getDocument()
        if (!customer.getDocument().equals(user.getDocument()) &&
                (user.getSystemRole().equals(RolUser.PersonCustomerUser) ||
                user.getSystemRole().equals(RolUser.CorporateCustomerUser))) {
            throw new BussinesException("No puedes ver estos préstamos, no eres el titular");
        }

        // Si es CorporateEmployee validar con user.getCustomer().getDocument()
        if (user.getSystemRole().equals(RolUser.CorporateEmployee) || user.getSystemRole().equals(RolUser.CorporateSupervisor)) {
            if (!customer.getDocument().equals(user.getCustomer().getDocument())) {
                throw new BussinesException("No puedes ver estos préstamos, la empresa no es la titular");
            }
        }
        return loanPort.findByCustomer(customer);
    }

    public List<Loan> findMyLoans(User user) throws NotFoundException{
        Customer customer = user.getCustomer();
        if(customer == null){
            throw new NotFoundException("El usuario no tiene un cliente asociado");
        }
        return loanPort.findByCustomer(customer);
    }
}
