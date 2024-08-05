package com.accenture.academico.bankingsystem.integrate.service;

import com.accenture.academico.bankingsystem.domain.address.Address;
import com.accenture.academico.bankingsystem.dto.address.AddressRequestDTO;
import com.accenture.academico.bankingsystem.dto.address.AddressResponseDTO;
import com.accenture.academico.bankingsystem.exception.NotFoundException;
import com.accenture.academico.bankingsystem.repositories.AddressRepository;
import com.accenture.academico.bankingsystem.services.general.AddressService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class AddressServiceTest {
    @Mock
    private AddressRepository addressRepository;

    @InjectMocks
    private AddressService addressService;

    @BeforeEach
    void setup(){
        MockitoAnnotations.initMocks(this);
    }
    @Test
    void getAllAddress() {
        List<Address> addressList = Arrays.asList(
                new Address(UUID.randomUUID(), "58700-010", "872", "Rua do Prado", "Centro"),
                new Address(UUID.randomUUID(), "58703-000", "877", "Rua do Prado", "Liberdade")
        );

        when(addressRepository.findAll()).thenReturn(addressList);

        List<AddressResponseDTO> addressResponseDTOList = addressService.getAllAddress();

        assertEquals(2, addressResponseDTOList.size());
        assertEquals("58700-010", addressResponseDTOList.get(0).cep());
        assertEquals("58703-000", addressResponseDTOList.get(1).cep());
    }

    @Test
    void create() {
        AddressRequestDTO request = new AddressRequestDTO("58700-010", "872", "Rua do Prado", "Centro");
        Address savedAddress = new Address(UUID.randomUUID(), request.cep(), request.number(), request.street(), request.district());

        when(addressRepository.save(any())).thenReturn(savedAddress);

        addressService.create(request);

        verify(addressRepository, times(1)).save(any());
    }

    @Test
    void update() {
        UUID uuid = UUID.randomUUID();
        Address address = new Address(uuid, "58700-010", "872", "Rua do Prado", "Centro");
        AddressRequestDTO request = new AddressRequestDTO("58700-011", "873", "Rua Nova", "Centro Novo");

        when(addressRepository.findById(uuid)).thenReturn(java.util.Optional.of(address));

        addressService.update(uuid, request);

        verify(addressRepository, times(1)).save(any());
    }

    @Test
    void updateAddressNotFound(){
        UUID uuid = UUID.randomUUID();

        when(addressRepository.findById(uuid)).thenReturn(java.util.Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class, () -> {
            addressService.delete(uuid);
        });

        assertEquals("Address not found with ID:" + uuid, exception.getMessage());

        verify(addressRepository, times(0)).save(any());
    }

    @Test
    void delete() {
        UUID uuid = UUID.randomUUID();
        Address address = new Address(uuid, "58700-010", "872", "Rua do Prado", "Centro");

        when(addressRepository.findById(uuid)).thenReturn(java.util.Optional.of(address));

        addressService.delete(uuid);

        verify(addressRepository, times(1)).delete(any());
    }

    @Test
    void deleteAddressNotFound(){
        UUID uuid = UUID.randomUUID();

        when(addressRepository.findById(uuid)).thenReturn(java.util.Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class, () -> {
            addressService.delete(uuid);
        });

        assertEquals("Address not found with ID:" + uuid, exception.getMessage());

        verify(addressRepository, times(0)).delete(any());
    }
}