package app.domain.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import app.domain.Exception.BussinesException;
import app.domain.models.CorporateCustomer;
import app.domain.models.PersonCustomer;
import app.domain.models.enums.CustomerStatus;
import app.domain.ports.CustomerPort;
import app.domain.ports.UserPort;

@Service
public class UpdateCorporateCustomer {
    private final CustomerPort customerPort;
    private final CreatePersonCustomer createPersonCustomer;
    private final UserPort userPort;

    @Autowired
    public UpdateCorporateCustomer(CustomerPort customerPort, CreatePersonCustomer createPersonCustomer, UserPort userPort){
        this.customerPort = customerPort;
        this.createPersonCustomer = createPersonCustomer;
        this.userPort = userPort;
    }

    public void updateCorporateCustomer(CorporateCustomer corporateCustomer) throws BussinesException {
        if (!customerPort.existsByDocument(corporateCustomer.getDocument())) {
            throw new BussinesException("No existe una empresa con dicho NIT");
        }

        if (corporateCustomer.getLegalRepresentative() != null) {
            String legalDoc = corporateCustomer.getLegalRepresentative().getDocument();

            if (!customerPort.existsByDocument(legalDoc)) {
                createPersonCustomer.createPersonCustomer(corporateCustomer.getLegalRepresentative());
            }

            // Buscar desde BD para tener el estado real
            PersonCustomer legal = (PersonCustomer) customerPort.findByDocument(legalDoc);
            if (legal == null) {
                throw new BussinesException("No se encontró el representante legal");
            }
            if (legal.getCustomerStatus() != CustomerStatus.Active) {
                throw new BussinesException("El representante legal no está activo en el sistema");
            }

            // Asignar el representante completo desde BD
            corporateCustomer.setLegalRepresentative(legal);
        }

        if (userPort.existsByDocument(corporateCustomer.getDocument())) {
            userPort.update(userPort.findByDocument(corporateCustomer.getDocument()));
        }

        customerPort.update(corporateCustomer);
    }
}