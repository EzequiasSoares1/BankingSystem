package com.accenture.academico.bankingsystem.integrate.service;

import com.accenture.academico.bankingsystem.config.ConfigSpringTest;
import com.accenture.academico.bankingsystem.domain.address.Address;
import com.accenture.academico.bankingsystem.dto.address.AddressRequestDTO;
import com.accenture.academico.bankingsystem.dto.address.AddressResponseDTO;
import com.accenture.academico.bankingsystem.exception.NotFoundException;
import com.accenture.academico.bankingsystem.repositories.AddressRepository;
import com.accenture.academico.bankingsystem.services.general.AddressService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;
import java.util.UUID;

public class AddressServiceTest implements ConfigSpringTest {

    @Autowired
    private AddressService addressService;

    @Autowired
    private AddressRepository addressRepository;

    private Address address;

    @BeforeEach
    void setUp() {
        address = new Address(UUID.randomUUID(), "58700-010", "872", "Rua do Prado", "Centro");
        address = addressRepository.save(address);
    }

    @Test

    void getAllAddress() {
        List<AddressResponseDTO> addressResponseDTOList = addressService.getAllAddress();

        Assertions.assertFalse(addressResponseDTOList.isEmpty());
        Assertions.assertEquals("58700-010", addressResponseDTOList.get(0).cep());
    }

    @Test
    @Order(1)
    void create() {
        AddressRequestDTO request = new AddressRequestDTO("58703-000", "877", "Rua do Prado", "Liberdade");

        AddressResponseDTO response = addressService.create(request);

        Assertions.assertNotNull(response.id());
        Assertions.assertEquals(request.cep(), response.cep());
        Assertions.assertEquals(request.number(), response.number());
        Assertions.assertEquals(request.street(), response.street());
        Assertions.assertEquals(request.district(), response.district());
    }

    @Test
    @Order(2)
    void update() {
        AddressRequestDTO request = new AddressRequestDTO("58703-001", "878", "Rua Nova", "Liberdade Nova");

        AddressResponseDTO response = addressService.update(address.getId(), request);

        Assertions.assertEquals(request.cep(), response.cep());
        Assertions.assertEquals(request.number(), response.number());
        Assertions.assertEquals(request.street(), response.street());
        Assertions.assertEquals(request.district(), response.district());
    }

    @Test
    @Order(3)
    void updateAddressNotFound() {
        UUID invalidId = UUID.randomUUID();
        AddressRequestDTO request = new AddressRequestDTO("58703-001", "878", "Rua Nova", "Liberdade Nova");

        NotFoundException exception = Assertions.assertThrows(NotFoundException.class, () -> {
            addressService.update(invalidId, request);
        });

        Assertions.assertEquals("Address not found with ID:" + invalidId, exception.getMessage());
    }

    @Test
    @Order(4)
    void delete() {
        addressService.delete(address.getId());

        NotFoundException exception = Assertions.assertThrows(NotFoundException.class, () -> {
            addressService.delete(address.getId());
        });

        Assertions.assertEquals("Address not found with ID:" + address.getId(), exception.getMessage());
    }

    @Test
    @Order(5)
    void deleteAddressNotFound() {
        UUID invalidId = UUID.randomUUID();

        NotFoundException exception = Assertions.assertThrows(NotFoundException.class, () -> {
            addressService.delete(invalidId);
        });

        Assertions.assertEquals("Address not found with ID:" + invalidId, exception.getMessage());
    }
}
