package com.accenture.academico.bankingsystem.integrate.services;

import com.accenture.academico.bankingsystem.config.ConfigIntegrateSpringTest;
import com.accenture.academico.bankingsystem.domain.address.Address;
import com.accenture.academico.bankingsystem.domain.agency.Agency;
import com.accenture.academico.bankingsystem.dtos.agency.AgencyRequestDTO;
import com.accenture.academico.bankingsystem.dtos.agency.AgencyResponseDTO;
import com.accenture.academico.bankingsystem.exceptions.ConflictException;
import com.accenture.academico.bankingsystem.exceptions.NotFoundException;
import com.accenture.academico.bankingsystem.repositories.AddressRepository;
import com.accenture.academico.bankingsystem.repositories.AgencyRepository;
import com.accenture.academico.bankingsystem.services.AddressService;
import com.accenture.academico.bankingsystem.services.AgencyService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;
public class AgencyServiceTestIntegrate implements ConfigIntegrateSpringTest {

    @Autowired
    private AgencyService agencyService;

    @Autowired
    private AgencyRepository agencyRepository;

    @Autowired
    private AddressService addressService;

    @Autowired
    private AddressRepository addressRepository;

    private Address address;
    private Agency agency;

    @BeforeEach
    void setUp() {
        address = new Address(UUID.randomUUID(), "58700-010", "872", "Rua do Prado", "Centro");
        address = addressRepository.save(address);

        agency = new Agency();
        agency.setName("Agência Centro");
        agency.setTelephone("123456789");
        agency.setNumber("001");
        agency.setAddress(address);
        agency = agencyRepository.save(agency);
    }

    @Test
    void getAllAgencies() {
        var agencies = agencyService.getAllAgencies();
        assertFalse(agencies.isEmpty());
    }

    @Test
    void getAgencyById() {
        var foundAgency = agencyService.getAgencyById(agency.getId());
        assertNotNull(foundAgency);
        assertEquals(agency.getName(), foundAgency.name());
    }

    @Test
    void createAgency() {
        Address addressRequest = new Address(UUID.randomUUID(),"58703-000", "877", "Rua do Prado", "Liberdade");
        var createdAddress = addressService.create(addressRequest);

        AgencyRequestDTO request = new AgencyRequestDTO(UUID.randomUUID(), "Agência Liberdade", "987654321","436354", createdAddress.getId());
        var createdAgency = agencyService.createAgency(request);

        assertNotNull(createdAgency);
        assertEquals(request.name(), createdAgency.name());
        assertEquals(request.number(), createdAgency.number());
        assertEquals(request.telephone(), createdAgency.telephone());
    }

    @Test
    void createAgency_Conflict() {
        AgencyRequestDTO request = new AgencyRequestDTO(UUID.randomUUID(), agency.getName(), agency.getTelephone(),agency.getNumber(), address.getId());

        assertThrows(ConflictException.class, () -> {
            agencyService.createAgency(request);
        });
    }

    @Test
    void updateAgency() {
        AgencyResponseDTO updateRequest = new AgencyResponseDTO(agency.getId(), "Agência Atualizada", "987654321", "002", address.getId());

        var updatedAgency = agencyService.updateAgency(agency.getId(), updateRequest);

        assertNotNull(updatedAgency);
        assertEquals(updateRequest.name(), updatedAgency.name());
        assertEquals(updateRequest.number(), updatedAgency.number());
        assertEquals(updateRequest.telephone(), updatedAgency.telephone());
    }

    @Test
    void updateNonExistentAgency() {
        UUID nonExistentId = UUID.randomUUID();
        AgencyResponseDTO updateRequest = new AgencyResponseDTO(nonExistentId, "Agência Atualizada", "987654321", "002", address.getId());

        assertThrows(NotFoundException.class, () -> {
            agencyService.updateAgency(nonExistentId, updateRequest);
        });
    }

    @Test
    void deleteAgency() {
        agencyService.deleteAgency(agency.getId());

        assertThrows(NotFoundException.class, () -> {
            agencyService.getAgencyById(agency.getId());
        });
    }

    @Test
    void deleteNonExistentAgency() {
        UUID nonExistentId = UUID.randomUUID();

        assertThrows(NotFoundException.class, () -> {
            agencyService.deleteAgency(nonExistentId);
        });
    }

    @Test
    void findById() {
        var foundAgency = agencyService.findById(agency.getId());
        assertNotNull(foundAgency);
        assertEquals(agency.getName(), foundAgency.getName());
    }

    @Test
    void findById_NotFound() {
        UUID nonExistentId = UUID.randomUUID();

        assertThrows(NotFoundException.class, () -> {
            agencyService.findById(nonExistentId);
        });
    }
}
