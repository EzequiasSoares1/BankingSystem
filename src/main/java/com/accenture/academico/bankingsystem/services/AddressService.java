package com.accenture.academico.bankingsystem.services;

import com.accenture.academico.bankingsystem.domain.address.Address;
import com.accenture.academico.bankingsystem.exceptions.NotFoundException;
import com.accenture.academico.bankingsystem.repositories.AddressRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AddressService {

    private final AddressRepository addressRepository;

    public List<Address> getAllAddress() {
        return addressRepository.findAll();
    }

    public Address getAddressById(UUID id){
        return this.addressRepository.findById(id).orElseThrow(
                () -> new NotFoundException("Address not found with ID:" + id));
    }

    public Address create(Address address){
        return addressRepository.save(address);
    }

    public Address update(UUID id, Address address){
        Address myAddress = this.getAddressById(id);

        if (address.getCep() != null) myAddress.setCep(address.getCep());

        if (address.getNumber() != null) myAddress.setNumber(address.getNumber());

        if (address.getStreet() != null) myAddress.setStreet(address.getStreet());

        if (address.getDistrict() != null) myAddress.setDistrict(address.getDistrict());

        return addressRepository.save(myAddress);
    }

    public void delete(UUID addressId){
        this.addressRepository.delete(this.getAddressById(addressId));
    }

}
