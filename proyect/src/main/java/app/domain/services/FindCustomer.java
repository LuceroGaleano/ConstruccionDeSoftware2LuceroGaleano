package app.domain.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import app.domain.Exception.NotFoundException;
import app.domain.models.Customer;
import app.domain.ports.CustomerPort;

@Service
public class FindCustomer {
    private CustomerPort customerPort;
    
    @Autowired
    public FindCustomer(CustomerPort customerPort){
        this.customerPort = customerPort;
    }

    public Customer findCustomer(String document) throws NotFoundException{
        Customer customer = customerPort.findByDocument(document);
        if(customer == null){
            throw new NotFoundException("Cliente no encontrado");   
        }

        return customer;
    }

    public List<Customer> findAll(){
        return customerPort.findAll();
    }
}
