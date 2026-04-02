package app.domain.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import app.domain.Exception.NotFoundException;
import app.domain.models.Customer;
import app.domain.models.Transfer;
import app.domain.ports.CustomerPort;
import app.domain.ports.TransferPort;

@Service
public class FindTransfer {
    private TransferPort transferPort;
    private CustomerPort customerPort;

    @Autowired
    public FindTransfer(TransferPort transferPort, CustomerPort customerPort){
        this.transferPort = transferPort;
        this.customerPort = customerPort;
    }

    public Transfer findById(String id) throws NotFoundException{
        Transfer transfer = transferPort.findById(id);
        if(transfer == null){
            throw new NotFoundException("Transferencia no encontrada");
        }
        return transfer;
    }

    public List<Transfer> findByCustomer(String customerDocument) throws NotFoundException{
        Customer customer = customerPort.findByDocument(customerDocument);
        if(customer == null){
            throw new NotFoundException("Cliente no encontrado");
        }
        return  transferPort.findByCustomer(customer);
    }
}
